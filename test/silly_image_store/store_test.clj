(ns silly-image-store.store-test
  (:require [clojure.test :refer :all]
            [silly-image-store.store :refer :all]))

(def test-basedir "test/fixtures/")

(deftest test-store
  (testing "loading a file"
    (testing "load existing file"
      (let [file (load-image test-basedir "test-all-the-things.jpg")]
        (is (= (class file) java.io.File))  
        (is (= (.getPath file) "test/fixtures/test-all-the-things.jpg"))))

    (testing "nil for inexistent image"
      (let [file (load-image "somewhere/" "doesnt-exist.png")]
        (is (nil? file)))))

  (testing "listing images"
    (is (= (list-images test-basedir) ["another-test.jpg" "test-all-the-things.jpg"]))))
