 (ns silly-image-store.handler
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(def images-dir
  (env :base-store-dir))

(defn serve-image [image-name]
  (let [image-file (io/file (str images-dir image-name))]
    (if (.exists image-file)
      image-file
      (route/not-found (str "No image '" image-name "' found")))))

(defroutes app-routes
  (GET "/image/:image-name" [image-name] (serve-image image-name))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

