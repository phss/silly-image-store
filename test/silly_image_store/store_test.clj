(ns silly-image-store.store-test
  (:require [clojure.test :refer :all]
            [silly-image-store.store :refer :all]))

(deftest test-store
  (testing "loading a file"
    (testing "load existing file"
      (let [file (load-image "resources/images/" "zelda.png")]
        (is (= (class file) java.io.File))  
        (is (= (.getPath file) "resources/images/zelda.png"))))

    (testing "nil for inexistent image"
      (let [file (load-image "somewhere/" "doesnt-exist.png")]
        (is (nil? file))))))
