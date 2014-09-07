(ns silly-image-store.handler-test
  (:require [clojure.test :refer :all]
            [silly-image-store.handler :refer :all]
            [ring.mock.request :as mock]))

(deftest test-app
  (testing "view image route"
    (testing "serve image"
      (let [response (app (mock/request :get "/image/test-all-the-things.jpg"))
            body (:body response)]
        (is (= (:status response) 200))  
        (is (= (class body) java.io.File))  
        (is (= (.getPath body) "test/fixtures/test-all-the-things.jpg"))))

    (testing "image not-found"
      (let [response (app (mock/request :get "/image/not-such-image.png"))]
        (is (= (:status response) 404))   
        (is (= (:body response) "No image 'not-such-image.png' found")))))

  (testing "generic not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
