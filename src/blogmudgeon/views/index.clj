(ns blogmudgeon.views.index
  (:use [hiccup.core]
        [markdown.core])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.data.json :as json]
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

;; TO USE: [:img.spinner {:src "/images/spinner.svg" :width 25 :height 25}]
;; (or however big you want it)
(defn view-spinner []
  (html
   [:svg {:xmlns "http://www.w3.org/2000/svg" :xmlns:xlink "http://www.w3.org/1999/xlink"
          :width "25px" :height "25px" :viewBox "0 0 100 100"}
    (let [blades 12
          r1 25
          r2 50
          thickness 12]
      (for [angle (range 1 360 (/ 360 blades))]
        [:rect {:x (+ 50 r1)
                :y (- 50 (/ thickness 2))
                :width (- r2 r1)
                :height thickness
                :style (str "fill: #333; fill-opacity: " (/ angle 360.0))
                :transform (str "rotate(" angle ", 50, 50)")}]))]))

(defn view-search [request]
  ;; TODO: add timestamp to these, somehow.
  ;; FUTURE: restrict results to this blog_id
  (let [query ((request :params) :query)
        results (jdbc/query db/db-spec ["SELECT id, title, created, updated FROM posts WHERE published=? AND to_tsvector(title || ' ' || content) @@ to_tsquery(?) LIMIT ?;" true query 5])]
    (json/write-str
     {:count (count results)
      :html (html
             [:ul
              (for [post results]
                [:li [:a {:href (str "/posts/" (post :id))} (post :title)]])])})))

(defn view-about []
  (layout/layout
   ((db/blog-info) :title)
    (html
     ;; TEMPORARY?: assumes docs[about] exists, not null, etc.
     (markdown.core/md-to-html-string
      ((first (jdbc/query db/db-spec ["SELECT content FROM docs WHERE slug = ? LIMIT 1;" "about"]))
       :content)))
    :about))
