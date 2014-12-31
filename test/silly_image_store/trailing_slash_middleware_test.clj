(ns silly-image-store.trailing-slash-middleware-test
  (:require [clojure.test :refer :all]
            [silly-image-store.trailing-slash-middleware :refer :all]))

(deftest test-trailing-slash-middleware
  (def middleware (ignore-trailing-slash identity))

  (testing "remove trailing slash from uri"
    (is (= "/no-slash" (:uri (middleware {:uri "/no-slash"}))))
    (is (= "/remove-slash" (:uri (middleware {:uri "/remove-slash/"}))))
    (is (= "/" (:uri (middleware {:uri "/"})))) ))
