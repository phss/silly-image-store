 (ns silly-image-store.handler
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(def images-dir
  (env :base-store-dir))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/image/:image-name" [image-name]
    (io/file (str images-dir image-name)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

