(ns blogmudgeon.views.index
  (:use [hiccup.core]
        [markdown.core])
  (:require [clojure.java.jdbc :as jdbc]
            [blogmudgeon.views.utils :as utils]
            [blogmudgeon.views.layout :as layout]))

;; KLUGE: also in layout.clj!  where to put this?
(def pgdb {:subprotocol "postgresql"
           :subname "//localhost:5432/kenmudgeon"}) ;; FIXME: get db name from ??

;; FIXME: the main page should show entire articles.  i'll need a "summary" view (e.g., for search), but i won't use it here.
(defn view-index []
  (layout/layout
   ((blog-info) :title)
   (html
    [:div.row
     ;; most recent 5 articles
     (interpose [:hr]
                (for [post (jdbc/query pgdb ["select * from posts where published=TRUE order by updated DESC limit 5"])]
                  (utils/summary-block  ;; FIXME: this fn is fucked...
                   5
                   (post :title)
                   (markdown.core/md-to-html-string (post :content))
                   "#"
                   "FIXME")))])
   :home))

;; ASSUMES there's only one blog in this db!
(defn blog-info []
  (first (jdbc/query pgdb ["select * from blogs limit 1;"])))

(defn view-about []
  (layout/layout
   ((blog-info) :title)
    (html
     ;; TEMPORARY?: assumes docs[about] exists, not null, etc.
     (markdown.core/md-to-html-string
      ((first (jdbc/query pgdb ["select content from docs where slug = 'about' limit 1;"]))
       :content)))
    :about))
