(ns silly-image-store.handler-test
  (:require [clojure.test :refer :all]
            [silly-image-store.handler :refer :all]
            [ring.mock.request :as mock]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "serve image"
    (let [response (app (mock/request :get "/image/zelda.png"))]
      (is (= (:status response) 200))))

  (testing "env"
    (let [response (app (mock/request :get "/env"))]
      (is (= (:status response) 200))  
      (is (= (:body response) "blah"))))
  
  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
