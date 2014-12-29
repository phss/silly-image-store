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

    (testing "handle multiple dirs without trailing slash"
      (let [file (load-image "test" "fixtures" "test-all-the-things.jpg")]
        (is (= (class file) java.io.File))  
        (is (= (.getPath file) "test/fixtures/test-all-the-things.jpg"))))

    (testing "nil for inexistent image"
      (let [file (load-image "somewhere/" "doesnt-exist.png")]
        (is (nil? file)))))

  (testing "random image"
    (let [file-path (.getPath (random-image test-basedir))]
      (is (some #{file-path} ["test/fixtures/test-all-the-things.jpg"
                              "test/fixtures/another-test.jpg"]))))

  (testing "listing images"
    (is (= (list-images test-basedir) ["another-test.jpg" "test-all-the-things.jpg"]))
    (is (= (list-images test-basedir "some-bucket") ["all-the-stuff.jpg" "stuff.jpg"]))
    (is (= (list-images "no-such-place") nil))))
