(ns blogmudgeon.views.layout
  (:use [hiccup.core])
  (:require [clojure.java.jdbc :as jdbc]
            [blogmudgeon.views.utils :as utils]
            [blogmudgeon.config :as config])
  (:import [java.util Calendar]))

;; FIXME: where should this be, really?
(def pgdb {:subprotocol "postgresql"
           :subname "//localhost:5432/kenmudgeon"}) ;; FIXME: get db name from config.clj!

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
    [:a.navbar-brand {:href config/SITE-ROOT-PATH}
     ((first (jdbc/query pgdb ["select * from blogs limit 1;"])) :title)]  ;; FIXME: extract blog-info into fn...
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
          ;; FUTURE: separate "recently updated" and "recently created", somehow...
          ;; FIXME?: something better than raw sql string?
          (for [post (jdbc/query pgdb ["select * from posts where published=TRUE order by updated DESC limit 5"])]
            [:li [:a {:href (str "/posts/" (post :id))} (post :title)]])
         ]]]]]

     [:footer.footer
      [:div.container.text-center
       [:p.text-muted.credit
        "Copyright (C) "
        (.get (Calendar/getInstance) Calendar/YEAR)
        " "
        ((first (jdbc/query pgdb ["select * from users limit 1;"])) :name)]]]]]))
