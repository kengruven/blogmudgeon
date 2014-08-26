(ns blogmudgeon.core
  (:use [compojure.core])
  (:require [ring.adapter.jetty :as ring]
            [clojure.java.jdbc :as jdbc]
            [blogmudgeon.db.db :as db]
            [blogmudgeon.config :as config]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            (compojure
              [route :as route]
              [handler :as handler])
            (blogmudgeon.views
              [index :as index])))

(defn db-users [login]
  (if-let [user (first (jdbc/query config/db-spec
                                   ["SELECT * FROM users WHERE login = ? LIMIT 1;" login]))]
    (conj (clojure.set/rename-keys user {:login :username})
          [:roles #{::user}])))  ;; FUTURE: #{::admin}, too?

(defroutes main-routes
  (GET "/" [] (index/view-index))
  (GET "/about" [] (index/view-about))
  (GET "/posts/:id" [id] (index/view-post id))
  (GET "/feeds/atom.xml" [] {:status 200
                             :headers {"Content-Type" "application/atom+xml"}
                             :body (index/view-atom-feed)})
  (POST "/posts/search" request (index/view-search request))

  (GET "/login" request (index/view-login request))
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/")))  ;; (from friend's README)

  ;; FOR TESTING ONLY:
  ;; (GET "/secret" request
  ;;      (friend/authorize #{::user} "This page can only be seen by authenticated users."))


  (GET "/images/spinner.svg" [] {:status 200
                                 :headers {"Content-Type" "image/svg+xml"}
                                 :body (index/view-spinner)})

  ;; HACK:
  ;; - lein-ring "server" defaults to using "public/" for compojure routing here.
  ;; --- but setting {:root "resources"} for the route/resources call doesn't work, for some reason.
  ;; - lein-ring "uberjar" packages "resources/" in the jar.
  ;; --- but i can't figure out how to change this, or even where it's done!
  ;; so for now, resources/public is a symlink -> public/.  ugh.
  (route/resources "/")

  (route/not-found "Page not found"))

(def app
  (handler/site
   (friend/authenticate main-routes {:credential-fn (partial creds/bcrypt-credential-fn db-users)
                                     :workflows [(workflows/interactive-form)]})))

;(defn start []
;  (ring/run-jetty main-routes {:port 8080 :join? false}))
