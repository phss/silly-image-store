 (ns silly-image-store.handler
  (:require [silly-image-store.store :as store]
            [silly-image-store.logging :refer :all]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [ring.middleware.json :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(def images-dir
  (env :base-store-dir))

(defn- image-not-found [image]
  (route/not-found (str "No image '" image "' found")))

(defn- bucket-not-found [bucket]
  (route/not-found (str "No bucket '" bucket "' found")))

(defn- request-url [{scheme :scheme, {host "host"} :headers, uri :uri}]
  (str (name scheme) "://" host uri))

(defn- list-images-route [request bucket]
  (let [base-image-url (request-url request)
        image-names (store/list-images images-dir bucket)
        to-json (fn [n] {:name n :url (str base-image-url "/" n)})]
    (if image-names 
      (map to-json image-names)
      (bucket-not-found bucket))))

(defn- serve-image-route [{{image :image} :params}]
  (let [image-file (store/load-image images-dir image)]
    (or image-file (image-not-found image))))

(defn- serve-random-image-route [request]
  (let [image-file (store/random-image images-dir)]
    (or image-file (image-not-found "random"))))


(defroutes app-routes
  (GET "/images" request (list-images-route request ""))
  (GET "/images/:image" request serve-image-route)
  (GET "/random" request serve-random-image-route)
  (GET "/buckets/:bucket/images" [bucket :as request] (list-images-route request bucket))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-json-response)
      (wrap-request-logging)))

