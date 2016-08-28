(ns offcourse.styles.components.cards
  (:refer-clojure :exclude [+ - * /])
  (:require [garden
             [arithmetic :refer [* +]]
             [stylesheet :refer [at-media]]]
            [garden.selectors :as s]
            [offcourse.styles.vocabulary :as v]))

(defn calculate-breakpoint [{:keys [min-width max-width column-count]} {:keys [column-gap column]}]
  (at-media {:min-width min-width :max-width max-width}
            (let [gap-count    (dec column-count)
                  actual-width (+ (* column-count column) (* gap-count column-gap))]
              [v/cards {:display      (if (= column-count 0) :none :block)
                        :column-count column-count
                        :min-width    actual-width
                        :max-width    actual-width}])))

(defn cards [{:keys [templates breakpoints borders colors units]}]
  [[v/cards (merge (:row-component templates)
                   (:recycled-paper templates)
                   {:padding          (:full units)
                    :column-gap       (:column-gap units)})
    [v/container {:display :inline-block
                  :width (:column units)
                  :padding-right (:third units)}]]

   (for [breakpoint breakpoints] (calculate-breakpoint breakpoint units))

   [v/card                      (merge (:column-component    templates)
                                       (:paper               templates)
                                       {:position            :relative
                                        :padding           [[0 0]]
                                        :flex                1})
    [v/hovered                         (:highlighted         borders)
     [:.card--backside          (merge (:negative            templates)
                                       (:column-component    templates)
                                       {:position            :absolute
                                        :height              "100%"
                                        :width               "100%"
                                        :background         (:dark colors)})]]]
    [:.card--backside                  {:display             :none
                                        :position            :relative}]

   [v/card--section             (merge (:row-component       templates)
                                       (:border-thin         templates)
                                       {:padding          [[(:full units)]]})]

   [:.card--frontside                  
    [v/card--section
     [v/first                   (merge {:padding          [[(:full units) (:one-and-half units) (:full units) (:full units)]]})]
     [v/second                  (merge {:justify-content     :space-between
                                        :align-items         :center
                                        :padding          [[(:half units) (:full units)]]})]
     [v/third                   (merge {:padding          [[(:full units) (:full units) 0 (:full units)]]
                                        :border              :none })]
     [v/last                           {:justify-content     :space-between}]]]

   [:.card--title                      (:title               templates)]

   [:.card--rating              (merge (:row-component       templates))]
   [:.card--rating-dot          (merge {:width              (:third units)
                                        :height             (:third units)
                                        :margin            [[0 (:third units) 0 0]]
                                        :border-radius      "100%"
                                        :background         (:medium colors)})
    [(s/& (s/attr :data-dot-active := "true")) {:background (:yellow colors)}]]   

   [:.card--following           (merge (:row-component       templates)
                                       (:label               templates)
                                       (:border-thin         templates)
                                       {:align-items         :center
                                        :justify-content     :center
                                        :padding          [[(:third units) (:half units)]]
                                        :background         (:blue colors)})]

   [:.card--picked              (merge {:height             (:full units)})]

   [:.card--earmark             (merge {:position            :absolute
                                        :top                 0
                                        :right               0
                                        :width              (:one-and-half units)
                                        :height             (:one-and-half units)
                                        :display             :flex
                                        :align-items         :center
                                        :justify-content     :center
                                        :background         (str "linear-gradient(45deg, " (:red colors) " 0%, " (:red colors) " 50%," (:light colors) " 51%)")
                                        :color              (:day    colors)})]

   [:.card--button              (merge (:row-component       templates)
                                       (:recycled-paper      templates)
                                       (:subtitle            templates)
                                       (:negative            templates)
                                       {:font-size          (:subtitle-font units)
                                        :padding          [[(:third units) (:two-third units)]]
                                        :cursor              :pointer })
    [v/hovered                         (:paper               templates)]]

   [:.card--info-corner         (merge (:subtitle            templates)
                                       {:padding          [[(:third units) (:two-third units)]]
                                        :display             :flex
                                        :align-items         :center
                                        :justify-content     :center
                                        :background         (:light colors)
                                        :color              (:night    colors)})
    [v/hovered                         (:negative            templates)]]


   [:.card--backside                  
    [v/card--section            (merge {:border-color       (:night colors)})
     [v/second                         {:align-items         :stretch}]
     [v/last                           {:border              :none}]]
    [v/label                           (:negative            templates)
     [v/hovered                        (:paper               templates)]]]

   [:.card--meta                (merge (:column-component    templates)
                                       {:padding          [[ 0 (* (:third units) 2)]]})]
   [:.card--img                        {:width              (* (:third units) 8)
                                        :height             (* (:third units) 8)}]
   [:.card--author              (merge (:subtitle            templates)
                                       {:color              (:blue colors)})]
   [:.card--author-status       (merge (:label               templates)
                                       {:padding-bottom    [[0 0 (:third units) 0]]})]
   [:.card--stats               (merge (:row-component       templates)
                                       (:justify-content     :space-between))]
   [:.card--stats-num           (merge (:smalltitle          templates)
                                       {:color              (:yellow colors)
                                        :padding           [[0 (:third units)]]})]
   [:.card--stats-title         (merge (:label               templates))]
   [:.card--text                       (:label                templates)]

   [:.card--smalltitle                 (:smalltitle          templates)
                                       {:color              (:yellow colors)}]
   [:.card--smalltext                  (:label               templates)
                                       {:color              (:blue colors)}]])
