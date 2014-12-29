(ns silly-image-store.store
  (:require [clojure.java.io :as io]))

(def file? #(.isFile %))
(def filename #(.getName %))

(defn load-image [& paths]
  (let [file (apply io/file paths)]
    (if (.exists file) file nil)))


(defn list-images [& paths]
  (->> (apply load-image paths)
       .listFiles
       (filter file?)
       (map filename)))

(defn random-image [basedir]
  (let [random-image-name (rand-nth (list-images basedir))]
    (load-image basedir random-image-name)))

