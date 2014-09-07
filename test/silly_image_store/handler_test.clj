(ns silly-image-store.handler-test
  (:require [clojure.test :refer :all]
            [silly-image-store.handler :refer :all]
            [ring.mock.request :as mock]))

(deftest test-app
  (testing "serve image"
    (let [response (app (mock/request :get "/image/zelda.png"))
          body (:body response)]
      (is (= (:status response) 200))  
      (is (= (class body) java.io.File))  
      (is (= (.getPath body) "resources/images/zelda.png"))))

  (testing "image not-found"
    (let [response (app (mock/request :get "/image/not-such-image.png"))]
      (is (= (:status response) 404))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
