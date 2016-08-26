(ns offcourse.views.components.card
  (:require [offcourse.views.components.label :refer [labels]]
            [offcourse.views.components.item-list :refer [item-list]]
            [rum.core :as rum]
            [services.logger :as logger]))

(rum/defc card [{:keys [course-id goal course-slug checkpoints curator] :as course}
                {:keys [toggle-checkpoint] :as handlers}
                {:keys [course-url checkpoint-url] :as url-helpers}]
  [:.container
   [:.card
    [:.card--frontside
     [:.card--section
      [:a.card--title {:href (course-url curator course-slug)} goal]
      [:.card--earmark ]]
     [:.card--section
      [:.card--rating
       [:.card--rating-dot {:data-dot-active "true"} ]
       [:.card--rating-dot {:data-dot-active "true"} ]
       [:.card--rating-dot ]
       [:.card--rating-dot ]
       [:.card--rating-dot ]]
      [:.card--following "Following"]
      [:img.card--picked {:src "http://imageshack.com/a/img923/1755/lDeG76.png"}]]
     [:.card--section (item-list :todo checkpoints
                                 {:checkpoint-url (partial checkpoint-url curator course-slug)}
                                 {:toggle-checkpoint (partial toggle-checkpoint course-id)}
                                 (:trackable? (meta course)))]
     [:.card--section
      [:.card--button      "Start"]
      [:.card--info-corner "i"]]]
    [:.card--backside
     [:.card--section
      [:a.card--title {:href (course-url curator course-slug)} goal]
      [:.card--earmark ]]
     [:.card--section
      [:img.card--img {:src "http://placehold.it/150x150"}]
      [:.card--meta
       [:.div
        [:h6.card--author "John Diddididoe"]
        [:h6.card--author-status "Expert"]]
       [:.card--stats
        [:span.card--stats-title "Posts: "]
        [:span.card--stats-num "10"]
        [:span.card--stats-title " Learners: "]
        [:span.card--stats-num "40"]
                                        ; [:span.card--stats-title " Forked: "]
                                        ; [:span.card--stats-num "5"]
        ]]]
     [:.card--section
      [:p.card--text "Leverage agile frameworks to provide a robust synopsis for high level overviews. Iterative approaches to corporate."]]
     [:.card--section (labels (:tags (-> course meta :tags)) url-helpers)]]]])

(rum/defc cards [{:keys [courses] :as data} actions url-helpers]
  [:.cards (map #(rum/with-key (card % actions url-helpers) (:course-id %)) courses)])
