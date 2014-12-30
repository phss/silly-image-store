(ns silly-image-store.store
  (:require [clojure.java.io :as io]))

(def exists? #(and % (.exists %)))
(def file? #(.isFile %))
(def directory? #(.isDirectory %))
(def filename #(.getName %))

(defn load-image [& paths]
  (let [file (apply io/file paths)]
    (if (exists? file) file)))

(defn- filtered-file-names [filter-fn paths]
  (let [image-directory (apply load-image paths)]
    (if (exists? image-directory)
      (->> image-directory
       .listFiles
       (filter filter-fn)
       (map filename)))))

(defn list-images [& paths]
  (filtered-file-names file? paths))

(defn list-image-dirs [& paths]
  (filtered-file-names directory? paths))

(defn random-image [& paths]
  (let [random-image-name (rand-nth (apply list-images paths))]
    (apply load-image (concat paths [random-image-name]))))

