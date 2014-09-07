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
    (let [response (app (mock/request :get "/image/zelda.png"))
          body (:body response)]
      (is (= (:status response) 200))  
      (is (= (class body) java.io.File))  
      (is (= (.getPath body) "resources/images/zelda.png"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
