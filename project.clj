(defproject silly-image-store "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [environ "1.0.0"]
                 [compojure "1.1.8"]]
  :plugins [[lein-ring "0.8.11"]
            [lein-environ "1.0.0"]]
  :ring {:handler silly-image-store.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]
         :env {:base-store-dir "resources/images/"}}})
