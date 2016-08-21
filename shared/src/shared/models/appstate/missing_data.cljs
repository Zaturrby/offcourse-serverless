(ns shared.models.appstate.missing-data
  (:require [clojure.set :as set]
            [shared.models.query.index :as query]
            [shared.protocols.queryable :as qa]
            [shared.protocols.validatable :as va]))

(defmulti missing-data (fn [state {:keys [viewmodel]}] (va/resolve-type viewmodel)))

(defmethod missing-data :resources [state {:keys [resources] :as viewmodel}]
  (let [state-urls  (into #{} (map :url (:resources state)))
        query-urls (into #{} (map :url resources))
        missing-urls (set/difference query-urls state-urls)
        missing-resources (map (fn [url] {:url url}) missing-urls)]
    (when-not (empty? missing-resources)
      #_missing-resources)))

(defmethod missing-data :collection [state {:keys [viewmodel]}]
  (query/create (:collection viewmodel)))

(defmethod missing-data :course [state {:keys [viewmodel]}]
  (let [course-query (-> viewmodel :course)
        course (qa/search state course-query)]
    (if (:checkpoints course)
      nil
      #_(va/missing-data state (payload/new :resources (map (fn [url] {:url url})
                                                            (qa/search course {:urls :all}))))
      (query/create course-query))))

(defmethod missing-data :checkpoint [state {:keys [viewmodel]}]
  (let [course-query (-> viewmodel :course)
        course (qa/search state course-query)]
    (if (:checkpoints course)
      nil
      #_(va/missing-data state (payload/new :resources (map (fn [url] {:url url})
                                                            (qa/search course {:urls :all}))))
      (query/create course-query))))

(defmethod missing-data :default [state proposal] nil)
