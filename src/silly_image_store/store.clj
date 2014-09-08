(ns silly-image-store.store
  (:require [clojure.java.io :as io]))

(defn load-image [basedir filename]
  (let [file (io/file (str basedir filename))]
    (if (.exists file) file nil)))

(defn list-images [basedir]
  (let [files (.listFiles (io/file basedir))]
    (map #(.getName %) files)))
