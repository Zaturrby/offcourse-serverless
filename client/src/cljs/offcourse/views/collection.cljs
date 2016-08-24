(ns offcourse.views.collection
  (:require [clojure.set :as set]
            [shared.protocols.decoratable :as dc]
            [plumbing.core :refer-macros [fnk]]))

(defn course-tags [course]
  (->> course
       :checkpoints
       (map :tags)
       (apply set/union)
       (into #{})))

(defn filter-courses [{:keys [collection-name collection-type]} courses]
  (case collection-type
    :curators (filter (fn [course] (= collection-name (:curator course))) courses)
    :flags (filter (fn [course] (set/superset? (into #{} (:flags course)) #{collection-name})) courses)
    :tags (filter (fn [course] (set/superset? (course-tags course) #{collection-name})) courses)
    courses))

(def graph
  {:collection (fnk [appstate] (get-in appstate [:viewmodel :collection]))
   :courses         (fnk [appstate user-name collection]
                         (->> (:courses appstate)
                              (map #(dc/decorate %1 user-name nil))
                              (filter-courses collection)))
   :main            (fnk [courses url-helpers handlers [:components cards]]
                         (cards courses url-helpers handlers))})
