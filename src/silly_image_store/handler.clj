 (ns silly-image-store.handler
  (:require [silly-image-store.store :as store]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [ring.middleware.json :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(def images-dir
  (env :base-store-dir))

(defn image-not-found [image]
  (route/not-found (str "No image '" image "' found")))

(defn list-images []
  (let [image-names (store/list-images images-dir)
        to-json (fn [n] {:name n})]
    (map to-json image-names)))

(defn serve-image [image]
  (let [image-file (store/load-image images-dir image)]
    (or image-file (image-not-found image))))

(defroutes app-routes
  (GET "/images" [] (list-images))
  (GET "/images/:image" [image] (serve-image image))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-json-response)))

