(ns offcourse.views.components.logo
  (:require [rum.core :as rum]))

(rum/defc logo [{:keys [site-title] :as data}
                actions
                {:keys [home-url] :as url-helpers}]
  [:.logo
   [:a {:href home-url} site-title]])
