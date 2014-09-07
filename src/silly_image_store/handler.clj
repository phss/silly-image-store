 (ns silly-image-store.handler
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))


(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/env" [] (env :base-store-dir))
  (GET "/image/:id" [id]
    (io/file (str "resources/images/" id)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

