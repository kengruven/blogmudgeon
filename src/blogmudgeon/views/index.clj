(ns blogmudgeon.views.index
  (:use [hiccup.core]
        [hiccup.page]
        [markdown.core])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.data.json :as json]
            [blogmudgeon.views.utils :as utils]
            [blogmudgeon.views.layout :as layout]
            [blogmudgeon.db.db :as db]
            [blogmudgeon.config :as config]))

(defn view-index []
  (layout/layout
   ((db/blog-info) :title)
   (html
    [:div.row
     (interpose [:hr]
                (for [post (jdbc/query config/db-spec ["SELECT * FROM posts WHERE published=? ORDER BY updated DESC LIMIT ?;" true 5])]
                  [:div
                   [:h2 (post :title)]
                   ;; TODO: show timestamp(s) here, too!
                   (markdown.core/md-to-html-string (post :content))]))])
   :home))

(defn first-paragraph [markdown]
  ;; in 'single-line mode' (i.e., match newlines as any other char),
  ;; any sequence of chars that doesn't end with two spaces, and
  ;; then is followed by a newline, an empty line (whitespace only),
  ;; then another newline.
  (or (second (re-find #"(?s)(.*?)(?<!  )\n\s*\n" markdown))
      markdown))

(defn as-iso-8601 [date]
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssZ") date))

(defn view-atom-feed []
  (let [blog (db/blog-info)
        user (db/user-info)]
    (html
     (xml-declaration "UTF-8")
     [:feed {:xmlns "http://www.w3.org/2005/Atom"}
      [:title (blog :title)]
      [:subtitle (blog :subtitle)]
      [:link {:href (str config/blog-host "feeds/atom.xml") :rel "self"}]  ;; FIXME: more robust url-building?
      [:link {:href (str config/blog-host)}]
      [:id (str "urn:uuid:" (blog :uuid))]
      [:updated (as-iso-8601
                 ((first (jdbc/query config/db-spec ["SELECT MAX(updated) AS updated FROM posts WHERE published=?;" true])) :updated))]

      ;; FUTURE: should i limit this to the most recent 100, or only updates from the past 3 months, or such?
      (for [post (jdbc/query config/db-spec ["SELECT * FROM posts WHERE published=? ORDER BY updated DESC;" true])]
        [:entry
         [:title (post :title)]
         [:link {:href (str config/blog-host "posts/" (post :id))}]
         [:updated (as-iso-8601 (post :updated))]
         [:id (str "urn:uuid:" (post :uuid))]
         [:content {:type "xhtml"}
          (markdown.core/md-to-html-string (first-paragraph (post :content)))]
         [:author
          [:name (user :name)]
          [:email (user :email)]]])])))

(defn view-post [id]
  (layout/layout
   ((db/blog-info) :title)
   (html
    [:div.row
     (let [id-int (try (Integer/parseInt id)
                       (catch NumberFormatException e -1))
           post (first (jdbc/query config/db-spec ["SELECT * FROM posts WHERE id = ? AND published = ? LIMIT 1;" id-int true]))]  ;; FUTURE: check blog_id, too.
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
  ;; FUTURE/PERF: this is not a pretty query...
  (let [query ((request :params) :query)
        results (jdbc/query config/db-spec ["SELECT id, title, ts_rank(to_tsvector(title || ' ' || content), to_tsquery(?)) AS rank FROM posts WHERE published=? AND to_tsquery(?) @@ to_tsvector(title || ' ' || content) ORDER BY rank DESC LIMIT ?;" query true query 5])]
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
      ((first (jdbc/query config/db-spec ["SELECT content FROM docs WHERE slug = ? LIMIT 1;" "about"]))
       :content)))
    :about))
