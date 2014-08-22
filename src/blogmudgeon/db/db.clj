(ns blogmudgeon.db.db
  (:require [clojure.java.jdbc :as jdbc]
            [blogmudgeon.config :as config]))

;; ASSUMES there's only one blog in this db!
(defn blog-info []
  (first (jdbc/query config/db-spec ["SELECT * FROM blogs LIMIT 1;"])))

;; ASSUMES there's only one user in this db!
(defn user-info []
  (first (jdbc/query config/db-spec ["SELECT * FROM users LIMIT 1;"])))

;; TODO: add other common db ops here...
