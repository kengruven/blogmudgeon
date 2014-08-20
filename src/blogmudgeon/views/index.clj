(ns blogmudgeon.views.index
  (:use [hiccup.core]
        [markdown.core])
  (:require [clojure.java.jdbc :as jdbc]
            [blogmudgeon.views.utils :as utils]
            [blogmudgeon.views.layout :as layout]
            [blogmudgeon.db.db :as db]))

(defn view-index []
  (layout/layout
   ((db/blog-info) :title)
   (html
    [:div.row
     (interpose [:hr]
                (for [post (jdbc/query db/db-spec ["SELECT * FROM posts WHERE published=? ORDER BY updated DESC LIMIT ?" true 5])]
                  [:div
                   [:h2 (post :title)]
                   ;; TODO: show timestamp(s) here, too!
                   (markdown.core/md-to-html-string (post :content))]))])
   :home))

(defn view-post [id]
  (layout/layout
   ((db/blog-info) :title)
   (html
    [:div.row
     (let [id-int (try (Integer/parseInt id)
                       (catch NumberFormatException e -1))
           post (first (jdbc/query db/db-spec ["SELECT * FROM posts WHERE id = ? AND published = ? LIMIT 1;" id-int true]))]  ;; FUTURE: check blog_id, too.
       (if post
         [:div
          [:h1 (post :title)]
          ;; TODO: show timestamp(s) here, too!
          (markdown.core/md-to-html-string (post :content))]
         (str "Error: no post with id \"" id "\".")))])  ;; FUTURE: make it a real 404?
   :post))

(defn view-about []
  (layout/layout
   ((db/blog-info) :title)
    (html
     ;; TEMPORARY?: assumes docs[about] exists, not null, etc.
     (markdown.core/md-to-html-string
      ((first (jdbc/query db/db-spec ["SELECT content FROM docs WHERE slug = ? LIMIT 1;" "about"]))
       :content)))
    :about))
