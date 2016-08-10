(ns styles.components.dashboard
  (:require [styles.vocabulary :as v]))

(defn dashboard [{:keys [templates colors units]}]
  [v/dashboard (merge (:column-component templates)
                      {:width (:sidebar units)})
   [v/container {:padding (:full units)}]])
