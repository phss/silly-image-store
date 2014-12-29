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


; Request/Response utils
(defn- request-url [{scheme :scheme, {host "host"} :headers, uri :uri}]
  (str (name scheme) "://" host uri))

(defn- json-list-response-builder [request]
  (let [base-url (request-url request)
        to-json (fn [n] {:name n :url (str base-url "/" n)})]
    (fn [names] (map to-json names))))


; Routes
(defn- not-found [thing]
  (route/not-found (str "No thing '" thing "' found")) 

(defn- list-images-route [request bucket]
  (let [json-list-response (json-list-response-builder request)
        image-names (store/list-images images-dir bucket)]
    (if image-names 
      (json-list-response image-names)
      (not-found bucket))))

(defn- serve-image-route [{{image :image} :params}]
  (let [image-file (store/load-image images-dir image)]
    (or image-file (not-found image))))

(defn- serve-random-image-route [request]
  (let [image-file (store/random-image images-dir)]
    (or image-file (not-found "random"))))

(defn- list-buckets-route [request]
  (let [json-list-response (json-list-response-builder request)
        bucket-names (store/list-image-dirs images-dir)]
    (json-list-response bucket-names)))


(defroutes app-routes
  (GET "/images" request (list-images-route request ""))
  (GET "/images/:image" request serve-image-route)
  (GET "/random" request serve-random-image-route)
  (GET "/buckets" request list-buckets-route)
  (GET "/buckets/:bucket/images" [bucket :as request] (list-images-route request bucket))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-json-response)
      (wrap-request-logging)))

