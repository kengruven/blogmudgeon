(ns blogmudgeon.views.layout
  (:use [hiccup.core]
        [hiccup.page])
  (:require [blogmudgeon.views.utils :as utils]
            [blogmudgeon.config :as config]))

(def nav
  {:home
    {:name "Home" :href "#"}
   :about
    {:name "About" :href "#about"}
   :contact
    {:name "Contact" :href "#contact"}})

(defn nav-with-active
  "Marks a nav element active"
  [nav active-one]
  (println active-one)
  (if (nil? active-one)
    nav
    (assoc-in nav [active-one :active] true)))


(defn layout-head [title]
  [:head
   [:title title]
   (utils/include-css "/css/bootstrap.min.css")
   (utils/include-css "/css/blogmudgeon.css")
   (utils/include-js "/js/jquery-2.1.1.min.js")
   (utils/include-js "/js/bootstrap.js")])

(defn nav-fixed [links]
  [:div.navbar.navbar-default {:id "blogmudgeon-navbar"}
   [:div.container
    [:div.navbar-header
     [:a.navbar-brand {:href config/SITE-ROOT-PATH} config/SITE-TITLE]  ;; FIXME: get title from config (db)
     [:ul.nav.navbar-nav
      (for [key (keys links)]
        (let [link (links key)]
          [:li
           (when (link :active) {:class "active"})
           [:a {:href (link :href)} (link :name)]]))]]]])

(defn layout [title body active-nav]
  (html
   "<!DOCTYPE html>"
   [:html
    (layout-head title)
    [:body
     (nav-fixed (nav-with-active nav active-nav))

     ;; NEW/TRYING: content area and sidebar!
     [:div.container  ;; BUG: div.container is the width of the page!
      [:div.row
       [:div.col-md-9 {:role "main"}
        [:div body]]
       [:div.col-md-3
        [:div.sidebar.hidden-print {:role "complementary"}
;;         [:div.well

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
          [:table.minical
           ;; TODO: generate this list from the past 5 weeks programmatically!  (and in a way that i can use with ajax.)
           ;; TODO: mark current day, too?
           [:tr
            [:th "S"] [:th "M"] [:th "T"] [:th "W"] [:th "R"] [:th "F"] [:th "S"]]
           [:tr
            [:td "13"] [:td "14"] [:td "15"] [:td "16"] [:td "17"] [:td "18"] [:td "19"]]
           [:tr
            [:td "20"] [:td "21"] [:td "22"] [:td "23"] [:td "24"] [:td "25"] [:td "26"]]
           [:tr
            [:td "27"] [:td "28"] [:td "29"] [:td "30"] [:td "31"] [:td "1"] [:td "2"]]
           [:tr
            [:td "3"] [:td "4"] [:td "5"] [:td "6"] [:td "7"] [:td "8"] [:td "9"]]
           [:tr
            [:td "10"] [:td "11"] [:td "12"] [:td "13"] [:td "14"] [:td "15"] [:td "16"]]]
          ]]]];;]

     ;; OLD:
     ;; [:div.container body]

     [:footer.footer
      [:div.container.text-center
       [:p.text-muted.credit
        "Copyright (C) YYYY  AUTHOR'S_NAME_HERE"]]]]]))
