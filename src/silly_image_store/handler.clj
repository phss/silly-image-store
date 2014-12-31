 (ns silly-image-store.handler
  (:require [silly-image-store.store :as store]
            [silly-image-store.logging :refer :all]
            [silly-image-store.trailing-slash-middleware :refer :all]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [ring.middleware.json :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(def images-dir (env :base-store-dir))
(def no-bucket "")


; Request/Response utils
(defn- request-url [{scheme :scheme, {host "host"} :headers, uri :uri}]
  (str (name scheme) "://" host uri))

(defn- list-response-builder [request]
  (let [base-url (request-url request)
        to-json (fn [n] {:name n :url (str base-url "/" n)})]
    (fn [names] (map to-json names))))

; Routes
(defn- not-found [thing]
  (route/not-found (str "No thing '" thing "' found")))

(defmacro if-exist [bucket expression]
  `(if (some #{~bucket} (store/list-image-dirs images-dir))
     ~expression
     (not-found ~bucket)))

(defn- root-route [request sep link-names]
  (let [base-url (request-url request)
        links (map (fn [n] {n (str base-url sep (name n))}) link-names)]
    {:body (into {} links)}))

(defn- list-images-route [request bucket]
  (let [list-response (list-response-builder request)
        image-names (store/list-images images-dir bucket)]
    (list-response image-names)))

(defn- serve-image-route [bucket image]
  (let [image-file (store/load-image images-dir bucket image)]
    (or image-file (not-found image))))

(defn- serve-random-image-route [bucket]
  (let [image-file (store/random-image images-dir bucket)]
    (or image-file (not-found "random"))))

(defn- list-buckets-route [request]
  (let [list-response (list-response-builder request)
        bucket-names (store/list-image-dirs images-dir)]
    (list-response bucket-names)))

(defroutes app-routes
  (GET "/" [:as request] (root-route request "" [:images :buckets :random]))
  (GET "/images" request (list-images-route request no-bucket))
  (GET "/images/:image" [image] (serve-image-route no-bucket image))
  (GET "/random" [] (serve-random-image-route no-bucket))
  (GET "/buckets" request list-buckets-route)
  (GET "/buckets/:bucket" [bucket :as request]
       (if-exist bucket (root-route request "/" [:images :random])))
  (GET "/buckets/:bucket/images" [bucket :as request] 
       (if-exist bucket (list-images-route request bucket)))
  (GET "/buckets/:bucket/images/:image" [bucket image] 
       (if-exist bucket (serve-image-route bucket image)))
  (GET "/buckets/:bucket/random" [bucket] 
       (if-exist bucket (serve-random-image-route bucket)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (ignore-trailing-slash)
      (wrap-json-response)
      (wrap-request-logging)))

