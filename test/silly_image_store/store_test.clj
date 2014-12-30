(ns silly-image-store.store-test
  (:require [clojure.test :refer :all]
            [silly-image-store.store :refer :all]))

(def test-basedir "test/fixtures/")

(deftest test-store
  (defn is-file-with-path [file path]
    (is (= (class file) java.io.File))  
    (is (= (.getPath file) path)))

  (testing "loading an image"
    (is-file-with-path (load-image test-basedir "tubes.jpg") "test/fixtures/tubes.jpg")
    (is-file-with-path (load-image "test" "fixtures" "tubes.jpg")"test/fixtures/tubes.jpg")
    (is (nil? (load-image "somewhere" "doesnt-exist.png"))))

  (testing "random image"
    (is (some #{(.getPath (random-image test-basedir))}
              ["test/fixtures/tubes.jpg"
               "test/fixtures/rocket.jpg"]))
    (is (some #{(.getPath (random-image test-basedir "some-bucket"))}
              ["test/fixtures/some-bucket/boxes.jpg"
               "test/fixtures/some-bucket/shed.jpg"])) )

  (testing "listing images"
    (is (= (list-images test-basedir) ["rocket.jpg" "tubes.jpg"]))
    (is (= (list-images test-basedir "some-bucket") ["boxes.jpg" "shed.jpg"]))
    (is (= (list-images "no-such-place") nil)))
  
  (testing "listing image directories"
    (is (= (list-image-dirs test-basedir) ["some-bucket"]))))
