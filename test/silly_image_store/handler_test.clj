(ns silly-image-store.handler-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [silly-image-store.handler :refer :all]
            [ring.mock.request :as mock]))

(deftest test-app
  (testing "view image route"
    (testing "serve image"
      (let [response (app (mock/request :get "/images/tubes.jpg"))
            body (:body response)]
        (is (= (:status response) 200))  
        (is (= (class body) java.io.File))  
        (is (= (.getPath body) "test/fixtures/tubes.jpg"))))

    (testing "image not-found"
      (let [response (app (mock/request :get "/images/not-such-image.png"))]
        (is (= (:status response) 404))   
        (is (= (:body response) "No thing 'not-such-image.png' found")))))

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
               [{"name" "rocket.jpg",
                 "url" "http://localhost/images/rocket.jpg"}
                {"name" "tubes.jpg",
                 "url" "http://localhost/images/tubes.jpg"}]))))
    
    (testing "images from bucket as json"
      (let [response (app (mock/request :get "/buckets/some-bucket/images"))
            json-body (json/read-str (:body response))]
        (is (= (:status response) 200))  
        (is (= json-body 
               [{"name" "boxes.jpg",
                 "url" "http://localhost/buckets/some-bucket/images/boxes.jpg"}
                {"name" "shed.jpg",
                 "url" "http://localhost/buckets/some-bucket/images/shed.jpg"}
                ]))))
    
    (testing "bucket not found"
      (let [response (app (mock/request :get "/buckets/no-such-bucket/images"))]
        (is (= (:status response) 404))   
        (is (= (:body response) "No thing 'no-such-bucket' found")))))

  (testing "listing buckets"
    (let [response (app (mock/request :get "/buckets"))
          json-body (json/read-str (:body response))]
      (is (= (:status response) 200))
      (is (= json-body [{"name" "some-bucket",
                         "url" "http://localhost/buckets/some-bucket"}]))))


  (testing "generic not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
