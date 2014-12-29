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

(defn- base-url [{scheme :scheme, {host "host"} :headers}]
  (str (name scheme) "://" host))

(defn- list-images-route [request]
  (let [image-names (store/list-images images-dir)
        image-url (str (base-url request) "/images/")
        to-json (fn [n] {:name n :url (str image-url n)})]
    (map to-json image-names)))

(defn- serve-image-route [{{image :image} :params}]
  (let [image-file (store/load-image images-dir image)]
    (or image-file (image-not-found image))))

(defn- serve-random-image-route [request]
  (let [image-file (store/random-image images-dir)]
    (or image-file (image-not-found "random"))))

(defn- list-images-from-bucket-route [{{bucket :bucket} :params, :as request}]
  (let [image-names (store/list-images images-dir bucket)
        image-url (str (base-url request) "/images/")
        to-json (fn [n] {:name n :url (str image-url n)})]
    (map to-json image-names)))


(defroutes app-routes
  (GET "/images" request list-images-route)
  (GET "/images/:image" request serve-image-route)
  (GET "/random" request serve-random-image-route)
  (GET "/buckets/:bucket/images" request list-images-from-bucket-route)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-json-response)
      (wrap-request-logging)))

