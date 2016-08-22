(ns offcourse.styles.index
  (:require [garden.def :refer [defstyles]]
            [offcourse.styles
             [config :refer [config]]
             [global :refer [global]]
             [layout :refer [layout]]
             [shame :refer [shame]]
             [typography :refer [typography]]]
            [offcourse.styles.components.index :refer [components]]))

(def base
  (let [modules [global typography layout components shame]]
    (for [module modules] (module config))))

(def documentation
  (let [modules [global typography layout components shame]]
    (for [module modules] (module config))))

(def client 
  (let [modules [global typography layout components shame]]
    (for [module modules] (module config))))