(ns silly-image-store.handler-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [silly-image-store.handler :refer :all]
            [ring.mock.request :as mock]))

(def do-get #(app (mock/request :get %)))

(deftest test-app
  (testing "root links"
    (testing "from base"
      (let [response (do-get "/")
            json-body (json/read-str (:body response))]
          (is (= (:status response) 200))  
          (is (= json-body {"images" "http://localhost/images",
                            "buckets" "http://localhost/buckets",
                            "random" "http://localhost/random"}))))

    (testing "from bucket"
      (let [response (do-get "/buckets/some-bucket")
            json-body (json/read-str (:body response))]
          (is (= (:status response) 200))  
          (is (= json-body {"images" "http://localhost/buckets/some-bucket/images",
                            "random" "http://localhost/buckets/some-bucket/random"})))))

  (testing "view image route"
    (testing "from base"
      (let [response (do-get "/images/tubes.jpg")
            body (:body response)]
        (is (= (:status response) 200))  
        (is (= (class body) java.io.File))  
        (is (= (.getPath body) "test/fixtures/tubes.jpg"))))

    (testing "from bucket"
      (let [response (do-get "/buckets/some-bucket/images/boxes.jpg")
            body (:body response)]
        (is (= (:status response) 200))  
        (is (= (class body) java.io.File))  
        (is (= (.getPath body) "test/fixtures/some-bucket/boxes.jpg")))))

  (testing "random image"
     (testing "from base"
      (let [response (do-get "/random")
            body (:body response)]
        (is (= (:status response) 200))  
        (is (= (class body) java.io.File))))

    (testing "from bucket"
      (let [response (do-get "/buckets/some-bucket/random")
            body (:body response)]
        (is (= (:status response) 200))  
        (is (= (class body) java.io.File)))))
    
    
  (testing "listing images"
    (testing "from base as json"
      (let [response (do-get "/images")
            json-body (json/read-str (:body response))]
        (is (= (:status response) 200))  
        (is (= json-body 
               [{"name" "rocket.jpg",
                 "url" "http://localhost/images/rocket.jpg"}
                {"name" "tubes.jpg",
                 "url" "http://localhost/images/tubes.jpg"}]))))
    
    (testing "from bucket as json"
      (let [response (do-get "/buckets/some-bucket/images")
            json-body (json/read-str (:body response))]
        (is (= (:status response) 200))  
        (is (= json-body 
               [{"name" "boxes.jpg",
                 "url" "http://localhost/buckets/some-bucket/images/boxes.jpg"}
                {"name" "shed.jpg",
                 "url" "http://localhost/buckets/some-bucket/images/shed.jpg"}
                ]))))
    )

  (testing "listing buckets"
    (let [response (do-get "/buckets")
          json-body (json/read-str (:body response))]
      (is (= (:status response) 200))
      (is (= json-body [{"name" "some-bucket",
                         "url" "http://localhost/buckets/some-bucket"}]))))

  (testing "image not found"
    (let [is-not-found (fn [resp] (is (and (= (:status resp) 404)
                                           (= (:body resp) "No thing 'no-such-image.jpg' found")))) ]
      (is-not-found (do-get "/images/no-such-image.jpg"))
      (is-not-found (do-get "/buckets/some-bucket/images/no-such-image.jpg"))))

  (testing "bucket not found"
    (let [is-not-found (fn [resp] (is (and (= (:status resp) 404)
                                           (= (:body resp) "No thing 'no-such-bucket' found")))) ]
      (is-not-found (do-get "/buckets/no-such-bucket"))
      (is-not-found (do-get "/buckets/no-such-bucket/images"))
      (is-not-found (do-get "/buckets/no-such-bucket/images/whatever.jpg"))
      (is-not-found (do-get "/buckets/no-such-bucket/random"))))

  (testing "generic not-found route"
    (let [response (do-get "/invalid")]
      (is (= (:status response) 404)))))
