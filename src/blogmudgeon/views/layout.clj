(ns blogmudgeon.views.layout
  (:use [hiccup.core])
  (:require [clojure.java.jdbc :as jdbc]
            [blogmudgeon.views.utils :as utils]
            [blogmudgeon.config :as config]
            [blogmudgeon.db.db :as db])
  (:import [java.util Calendar]))

(def nav
  {:home
    {:name "Home" :href "/"}
   :about
    {:name "About" :href "/about"}})

(defn layout-head [title]
  [:head
   [:title title]
   (utils/include-css "/css/bootstrap.min.css")
   (utils/include-css "/css/blogmudgeon.css")
   (utils/include-js "/js/jquery-2.1.1.min.js")
   (utils/include-js "/js/bootstrap.js")
   (utils/include-js "/js/blogmudgeon.js")
   [:link {:rel "alternate"
           :type "application/atom+xml"
           :title (str ((db/blog-info) :title) " Feed")
           :href "/feeds/atom.xml"}]])

(defn nav-fixed [active-nav]
  [:div.navbar.navbar-default {:id "blogmudgeon-navbar"}
   [:div.container
    [:a.navbar-brand {:href "/"} ((db/blog-info) :title)]
    [:ul.nav.navbar-nav.navbar-right
     [:li {:class (if (= active-nav "about") "active" "")}
      [:a {:href "/about"} "About"]]]]])

      ;; FUTURE: let the user configure any number of static pages as top-tabs (left/right-aligned), with any markdown content they want?  sounds good...

(defn layout [title body active-nav]
  (html
   "<!DOCTYPE html>"
   [:html
    (layout-head title)
    [:body
     (nav-fixed active-nav)

     [:div.container
      [:div.row
       [:div.col-md-9 {:role "main"}
        [:div body]]
       [:div.col-md-3
        [:div.sidebar.hidden-print {:role "complementary"}
         [:div.input-group.input-group-sm.blogmudgeon-search
          [:input.form-control {:type "text"}
           [:span.input-group-btn
            [:button.btn.btn-default {:type "button"} ""
             [:span.glyphicon.glyphicon-search]]]]]
         [:div.blogmudgeon-search-results
          [:h5 "Results"]
          [:div.target]]
         [:hr]
         [:h5 "Recent"]
         [:ul.recent-posts
          ;; FUTURE: separate "recently updated" and "recently created", somehow...
          (for [post (jdbc/query config/db-spec ["SELECT * FROM posts WHERE published=? ORDER BY updated DESC LIMIT ?;" true 5])]
            [:li [:a {:href (str "/posts/" (post :id))} (post :title)]])
         ]]]]]

     [:footer.footer
      [:div.container.text-center
       [:p.text-muted.credit
        "Copyright (C) "
        (.get (Calendar/getInstance) Calendar/YEAR)
        " "
        ((db/user-info) :name)]]]]]))
