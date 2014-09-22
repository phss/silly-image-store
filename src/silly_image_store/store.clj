(ns silly-image-store.store
  (:require [clojure.java.io :as io]))

(defn load-image [& paths]
  (let [file (apply io/file paths)]
    (if (.exists file) file nil)))

(defn list-images [basedir]
  (let [files (.listFiles (load-image basedir))]
    (map #(.getName %) files)))

(defn random-image [basedir]
  (let [random-image-name (rand-nth (list-images basedir))]
    (load-image basedir random-image-name)))

