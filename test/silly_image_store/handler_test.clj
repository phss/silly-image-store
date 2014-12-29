(ns silly-image-store.handler-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [silly-image-store.handler :refer :all]
            [ring.mock.request :as mock]))

(deftest test-app
  (testing "view image route"
    (testing "serve image"
      (let [response (app (mock/request :get "/images/test-all-the-things.jpg"))
            body (:body response)]
        (is (= (:status response) 200))  
        (is (= (class body) java.io.File))  
        (is (= (.getPath body) "test/fixtures/test-all-the-things.jpg"))))

    (testing "image not-found"
      (let [response (app (mock/request :get "/images/not-such-image.png"))]
        (is (= (:status response) 404))   
        (is (= (:body response) "No image 'not-such-image.png' found")))))

  (testing "serve random image"
    (let [response (app (mock/request :get "/random"))
          body (:body response)]
      (is (= (:status response) 200))  
      (is (= (class body) java.io.File))))
    
  (testing "listing images"
    (testing "images from base dir as json"
      (let [response (app (mock/request :get "/images"))
            json-body (json/read-str (:body response))]
        (is (= (:status response) 200))  
        (is (= json-body 
               [{"name" "another-test.jpg",
                 "url" "http://localhost/images/another-test.jpg"}
                {"name" "test-all-the-things.jpg",
                 "url" "http://localhost/images/test-all-the-things.jpg"}]))))
    
    (testing "images from bucket as json"
      (let [response (app (mock/request :get "/buckets/some-bucket/images"))
            json-body (json/read-str (:body response))]
        (is (= (:status response) 200))  
        (is (= json-body 
               [{"name" "all-the-stuff.jpg",
                 "url" "http://localhost/buckets/some-bucket/images/all-the-stuff.jpg"}
                {"name" "stuff.jpg",
                 "url" "http://localhost/buckets/some-bucket/images/stuff.jpg"}
                ])))))

  (testing "generic not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
