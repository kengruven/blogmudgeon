(ns blogmudgeon.views.index
  (:use [hiccup.core]
        [hiccup.page])
    (:require (blogmudgeon.views
                [utils :as utils]
                [layout :as layout])))

(defn view-index []
  (layout/layout
    "Welcome"
    (html
      [:div.jumbotron
       [:h1 "Hello, world!"]
       [:p "Vestibulum id ligula porta felis euismod semper. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit."]
       [:p [:a.btn.btn-default.btn-lg "Learn more &raquo;"]]]
      [:div.row
       (utils/summary-block
        5
        "Heading"
        "Etiam porta sem malesuada magna mollis euismod. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit."
        "#"
        "View details &raquo;")
       (utils/summary-block
        6
        "Heading"
        "Etiam porta sem malesuada magna mollis euismod. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit."
        "#"
        "View details &raquo;")
       (utils/summary-block
        5
        "Heading"
        "Etiam porta sem malesuada magna mollis euismod. Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit."
        "#"
        "View details &raquo;")])
    :home))