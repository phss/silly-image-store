(ns silly-image-store.store
  (:require [clojure.java.io :as io]))

(defn load-image [& paths]
  (let [file (io/file (apply str paths))]
    (if (.exists file) file nil)))

(defn list-images [basedir]
  (let [files (.listFiles (load-image basedir))]
    (map #(.getName %) files)))
