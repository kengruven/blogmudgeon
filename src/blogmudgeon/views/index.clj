(ns blogmudgeon.views.index
  (:use [hiccup.core]
        [hiccup.page]
        [markdown.core])
    (:require (blogmudgeon.views
                [utils :as utils]
                [layout :as layout])))

;; FIXME: the main page should show entire articles.  i'll need a "summary" view (e.g., for search), but i won't use it here.
(defn view-index []
  (layout/layout
    "Blogmudgeon"  ;; TODO: make this the blog title
    (html
      [:div.row
       (utils/summary-block
        5
        "Most recent post"
        "This is a blog I'm writing.  I'm writing both a blog (kenmudgeon.com) and the software that powers it (github.com/kengruven/blogmudgeon)."
        "#"
        "Full article &rarr;")
       [:hr]
       (utils/summary-block
        6
        "Second most recent post"
        "It doesn't really do anything yet."
        "#"
        "Nothing to see here &rarr;")
       [:hr]
       (utils/summary-block
        5
        "Third most recent post"
        "The point of this, so far, is to make sure I can commit, push, and deploy easily."
        "#"
        "Nothing to see here &rarr;")
       [:hr]
       (utils/summary-block
        5
        "Markdown test"
        (markdown.core/md-to-html-string "This string is *Markdown!*")
        "#"
        "Nothing to see here &rarr;")])
    :home))

(defn view-about []
  (layout/layout
    "Blogmudgeon"  ;; TODO: make this the blog title
    (html
     "WRITEME: about this blog")  ;; TODO: read from config (db)
    :about))
