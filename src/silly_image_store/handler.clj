 (ns silly-image-store.handler
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route])
  (:use [ring.util.response :only (file-response resource-response status)]))

(def images-dir
  (env :base-store-dir))

(defn serve-image [image-name]
  (let [image-file (io/file (str images-dir image-name))]
    (if (.exists image-file)
      image-file
      {:status 404})))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/image/:image-name" [image-name]
    (serve-image image-name))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

