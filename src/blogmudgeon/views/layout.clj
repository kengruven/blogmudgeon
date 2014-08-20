(ns blogmudgeon.views.layout
  (:use [hiccup.core]
        [hiccup.page])
  (:require [blogmudgeon.views.utils :as utils]
            [blogmudgeon.config :as config]))

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
   (utils/include-js "/js/bootstrap.js")])

(defn nav-fixed [active-nav]
  [:div.navbar.navbar-default {:id "blogmudgeon-navbar"}
   [:div.container
    [:a.navbar-brand {:href config/SITE-ROOT-PATH} config/SITE-TITLE]  ;; FIXME: get title from config (db)
    [:ul.nav.navbar-nav.navbar-right
     [:li {:class (if (= active-nav "about") "active" "")}
      [:a {:href "about"} "About"]]]]])

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
         [:div.input-group.input-group-sm
          [:input.form-control {:type "text" :style "border-top-left-radius:15px;border-bottom-left-radius:15px"}
           [:span.input-group-btn
            [:button.btn.btn-default {:type "button" :style "border-top-right-radius: 15px;border-bottom-right-radius: 15px"} ""
             [:span.glyphicon.glyphicon-search]]]]]
         [:hr]
         [:h5 "Recent"]
         [:ul.recent-posts
          [:li [:a {:href "/posts/1"} "Post 1"]]
          [:li [:a {:href "/posts/2"} "Post 2"]]
          [:li [:a {:href "/posts/17"} "Post 17"]]
          [:li [:a {:href "/posts/99"} "Post 99"]]]
         [:hr]
         [:h5 "Links"]
         [:ul
          [:li "WRITEME"]]]]]]  ;; FUTURE: read list from db

     [:footer.footer
      [:div.container.text-center
       [:p.text-muted.credit
        "Copyright (C) YYYY  AUTHOR'S_NAME_HERE"]]]]]))
