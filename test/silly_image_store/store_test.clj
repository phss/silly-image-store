(ns silly-image-store.store-test
  (:require [clojure.test :refer :all]
            [silly-image-store.store :refer :all]))

(def test-basedir "test/fixtures/")

(deftest test-store
  (testing "loading a file"
    (testing "load existing file"
      (let [file (load-image test-basedir "tubes.jpg")]
        (is (= (class file) java.io.File))  
        (is (= (.getPath file) "test/fixtures/tubes.jpg"))))

    (testing "handle multiple dirs without trailing slash"
      (let [file (load-image "test" "fixtures" "tubes.jpg")]
        (is (= (class file) java.io.File))  
        (is (= (.getPath file) "test/fixtures/tubes.jpg"))))

    (testing "nil for inexistent image"
      (let [file (load-image "somewhere" "doesnt-exist.png")]
        (is (nil? file)))))

  (testing "random image"
    (let [file-path (.getPath (random-image test-basedir))]
      (is (some #{file-path} ["test/fixtures/tubes.jpg"
                              "test/fixtures/rocket.jpg"]))))

  (testing "random image form bucket"
    (let [file-path (.getPath (random-image test-basedir "some-bucket"))]
      (is (some #{file-path} ["test/fixtures/some-bucket/boxes.jpg"
                              "test/fixtures/some-bucket/shed.jpg"]))))

  (testing "listing images"
    (is (= (list-images test-basedir) ["rocket.jpg" "tubes.jpg"]))
    (is (= (list-images test-basedir "some-bucket") ["boxes.jpg" "shed.jpg"]))
    (is (= (list-images "no-such-place") nil)))
  
  (testing "listing image directories"
    (is (= (list-image-dirs test-basedir) ["some-bucket"]))))
