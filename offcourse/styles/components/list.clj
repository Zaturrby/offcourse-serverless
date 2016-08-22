(ns offcourse.styles.components.list
  (:refer-clojure :exclude [first last list])
  (:require [offcourse.styles.vocabulary :as v]
            [garden.units :as u :refer [percent]]))

(defn list-component [{:keys [templates borders colors fonts units]}]

  [[v/list         (merge (:column-component templates)
                          {:width           (percent 100)})]
   [v/list--item   (merge (:row-component    templates)
                          (:recycled-paper   templates)
                          (:text             templates)
                          {:margin-bottom   (:sixth units)
                           :font-size       (:subtitle-font units)
                           :padding         (:third units)
                           :cursor           :pointer })
    [v/hovered            (:selected         templates)]]

   [v/edit-list
    [v/list--item  (merge {:justify-content :space-between
                           :padding 0
                           :background-color (:day colors)})
     [v/icon-button       {:font-size (:two-third units)}]
     [:span
      [v/first     (merge (:recycled-paper templates)
                          {:flex 1
                           :display :flex
                           :padding (:half units)
                           :align-items :center
                           :height (:one-and-half units)
                           :margin-right (:sixth units)})]]]]

   [v/todo-list
    [v/list--item         {:justify-content :flex-start}
     [v/hovered           (:selected templates)]
     [v/selected          (:highlighted templates)
      [v/checkbox-button
       [v/selected        {:background-color (:night colors)}]]]]]])

