(ns silly-image-store.store
  (:require [clojure.java.io :as io]))

(def exists? #(and % (.exists %)))
(def file? #(.isFile %))
(def filename #(.getName %))

(defn load-image [& paths]
  (let [file (apply io/file paths)]
    (if (exists? file) file)))


(defn list-images [& paths]
  (let [image-directory (apply load-image paths)]
    (if (exists? image-directory)
      (->> image-directory
       .listFiles
       (filter file?)
       (map filename)))))

(defn random-image [basedir]
  (let [random-image-name (rand-nth (list-images basedir))]
    (load-image basedir random-image-name)))

