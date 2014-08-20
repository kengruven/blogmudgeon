(ns blogmudgeon.views.utils
  (:use [hiccup.core]))

(defn include-css
  "creates a css stylesheet link"
  [href]
  [:link {:rel "stylesheet" :href href}])

(defn include-js
  "creates a javascript link"
  [src]
  [:script {:type "text/javascript" :src src}])

(defn inline-js
  "includes inline js"
  [content]
  [:script {:type "text/javascript"} content])

(defn inline-css
  "includes inline css"
  [content]
  [:style {:type "text/css"} content])
