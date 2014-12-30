(ns silly-image-store.store
  (:require [clojure.java.io :as io]))

(def exists? #(and % (.exists %)))
(def file? #(.isFile %))
(def directory? #(.isDirectory %))
(def filename #(.getName %))

(defn load-image [& path]
  (let [file (apply io/file path)]
    (if (exists? file) file)))

(defn- filtered-file-names [filter-fn path]
  (let [image-directory (apply load-image path)]
    (if (exists? image-directory)
      (->> image-directory
       .listFiles
       (filter filter-fn)
       (map filename)))))

(defn list-images [& path]
  (filtered-file-names file? path))

(defn list-image-dirs [& path]
  (filtered-file-names directory? path))

(defn random-image [& path]
  (let [random-image-name (rand-nth (apply list-images path))]
    (apply load-image (concat path [random-image-name]))))

