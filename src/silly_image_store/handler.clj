(ns silly-image-store.handler
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))


(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/image/:id" [id]
    (io/file (str "resources/images/" id)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

