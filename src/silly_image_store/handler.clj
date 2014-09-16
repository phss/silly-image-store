 (ns silly-image-store.handler
  (:require [silly-image-store.store :as store]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
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
    (log/info "Blah")
    (map to-json image-names)))

(defn- serve-image-route [{{image :image} :params}]
  (let [image-file (store/load-image images-dir image)]
    (or image-file (image-not-found image))))

(defroutes app-routes
  (GET "/images" request list-images-route)
  (GET "/images/:image" request serve-image-route)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-json-response)))

