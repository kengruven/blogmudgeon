(defproject blogmudgeon "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.47"]
                 [org.clojure/java.jdbc "0.3.5"]
                 [postgresql "9.1-901.jdbc4"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler blogmudgeon.core/app})
