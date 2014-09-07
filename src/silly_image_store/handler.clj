 (ns silly-image-store.handler
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(def images-dir
  (env :base-store-dir))

(defn load-image [basedir filename]
  (let [file (io/file (str basedir filename))]
    (if (.exists file) file nil)))

(defn image-not-found [image-name]
  (route/not-found (str "No image '" image-name "' found")))

(defn serve-image [image-name]
  (let [image-file (load-image images-dir image-name)]
    (or image-file (image-not-found image-name))))

(defroutes app-routes
  (GET "/image/:image-name" [image-name] (serve-image image-name))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

