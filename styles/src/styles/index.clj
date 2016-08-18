(ns styles.index
  (:require [garden.def :refer [defstyles]]
            [styles
             [config :refer [config]]
             [global :refer [global]]
             [layout :refer [layout]]
             [typography :refer [typography]]]
            [styles.components.index :refer [components]]))

(defn base []
  (let [modules [global typography layout components]]
    (for [module modules] (module config))))
