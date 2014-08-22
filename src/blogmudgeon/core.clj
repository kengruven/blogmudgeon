(ns blogmudgeon.core
  (:use [compojure.core])
  (:require [ring.adapter.jetty :as ring]
            (compojure
              [route :as route]
              [handler :as handler])
            (blogmudgeon.views
              [index :as index])))

(defroutes main-routes
  (GET "/" [] (index/view-index))
  (GET "/about" [] (index/view-about))
  (GET "/posts/:id" [id] (index/view-post id))
  (GET "/feeds/atom.xml" [] {:status 200
                             :headers {"Content-Type" "application/atom+xml"}
                             :body (index/view-atom-feed)})
  (POST "/posts/search" request (index/view-search request))

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
  (handler/site main-routes))

;(defn start []
;  (ring/run-jetty main-routes {:port 8080 :join? false}))
