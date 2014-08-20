(ns blogmudgeon.views.index
  (:use [hiccup.core]
        [markdown.core])
  (:require [clojure.java.jdbc :as jdbc]
            [blogmudgeon.views.utils :as utils]
            [blogmudgeon.views.layout :as layout]
            [blogmudgeon.db.db :as db]))

;; FIXME: the main page should show entire articles.  i'll need a "summary" view (e.g., for search), but i won't use it here.
(defn view-index []
  (layout/layout
   ((db/blog-info) :title)
   (html
    [:div.row
     ;; most recent 5 articles
     (interpose [:hr]
                (for [post (jdbc/query db/db-spec ["select * from posts where published=TRUE order by updated DESC limit 5"])]
                  (utils/summary-block  ;; FIXME: this fn is fucked...
                   5
                   (post :title)
                   (markdown.core/md-to-html-string (post :content))
                   "#"
                   "FIXME")))])
   :home))

(defn view-about []
  (layout/layout
   ((db/blog-info) :title)
    (html
     ;; TEMPORARY?: assumes docs[about] exists, not null, etc.
     (markdown.core/md-to-html-string
      ((first (jdbc/query db/db-spec ["select content from docs where slug = 'about' limit 1;"]))
       :content)))
    :about))
