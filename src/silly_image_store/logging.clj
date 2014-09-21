(ns silly-image-store.logging
  (:require [clojure.tools.logging :as log]))

(defn wrap-request-logging [handler]
  (fn [{requester :remote-addr uri :uri action :request-method :as request}]
    (log/info (str action " " uri " from " requester))
    (handler request)))
