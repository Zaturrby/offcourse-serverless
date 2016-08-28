(ns offcourse.views.collection
  (:require [clojure.set :as set]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]))

(defn filter-courses [{:keys [collection-name collection-type]} courses]
  (case collection-type
    "curators" (filter (fn [course]
                         (= collection-name (:curator course))) courses)
    "flags" (filter (fn [course]
                      (set/superset? (into #{} (:flags course)) #{collection-name})) courses)
    "tags" (filter (fn [course]
                     (set/superset? (-> course meta :tags) #{collection-name})) courses)))

(def graph
  {:collection (fnk [viewmodel] (get-in viewmodel [:collection]))
   :courses    (fnk [appstate user-name collection]
                    (->> (:courses appstate)
                         (map #(dc/decorate %1 user-name nil))
                         (filter-courses collection)))
   :actions    (fnk [base-actions handlers]
                    (->> handlers
                         (select-keys [:toggle-checkpoint])
                         (merge base-actions)))
   :main       (fnk [courses
                     actions
                     url-helpers
                     [:components cards]]
                    (cards {:courses courses} actions url-helpers))})
