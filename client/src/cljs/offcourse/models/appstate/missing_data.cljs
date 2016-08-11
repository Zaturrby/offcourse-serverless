(ns offcourse.models.appstate.missing-data
  (:require [offcourse.models.payload.index :as payload]
            [offcourse.protocols.queryable :as qa]
            [offcourse.protocols.validatable :as va]
            [cljs.spec :as spec]
            [clojure.set :as set]
            [cljs.spec :as spec]
            [specs.core :as specs]))


(defmulti missing-data (fn [state viewmodel] (first (spec/conform ::specs/viewmodel viewmodel))))

(defmethod missing-data :resources [state {:keys [resources] :as viewmodel}]
  (let [state-urls  (into #{} (map :url (:resources state)))
        query-urls (into #{} (map :url resources))
        missing-urls (set/difference query-urls state-urls)
        missing-resources (map (fn [url] {:url url}) missing-urls)]
    (when-not (empty? missing-resources)
      (payload/new :resources missing-resources))))

(defmethod missing-data :collection [state {:keys [collection]}] collection)

(defmethod missing-data :course [state {:keys [course] :as viewmodel}]
  (if (:checkpoints course)
    (va/missing-data state (payload/new :resources (map (fn [url] {:url url}) (qa/get course :urls {}))))
    (payload/new :course course)))

(defmethod missing-data :checkpoint [state {:keys [course] :as viewmodel}]
  (payload/new :course course))
