(defproject blogmudgeon "0.0.1-SNAPSHOT"
  :description "A simple little one-person blog"
  :url "http://github.com/kengruven/blogmudgeon"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]

                 ;; web serving
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.6"]

                 ;; text rendering
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.47"]

                 ;; database access
                 [org.clojure/java.jdbc "0.3.5"]
                 [postgresql "9.1-901.jdbc4"]
                 [org.clojure/data.json "0.2.5"]

                 ;; authentication
                 [com.cemerick/friend "0.2.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler blogmudgeon.core/app})
