(ns blogmudgeon.db.db
  (:require [clojure.java.jdbc :as jdbc]))

;; FIXME: get db name from blogmudgeon.config!
(def db-spec {:subprotocol "postgresql"
              :subname "//localhost:5432/kenmudgeon"})

;; ASSUMES there's only one blog in this db!
(defn blog-info []
  (first (jdbc/query db-spec ["SELECT * FROM blogs LIMIT 1;"])))

;; TODO: add other common db ops here...
