(ns offcourse.models.appstate.missing-data
  (:require [offcourse.models.payload.index :as payload]
            [offcourse.protocols.queryable :as qa]
            [offcourse.protocols.validatable :as va]
            [cljs.spec :as spec]
            [clojure.set :as set]
            [shared.models.query.index :as query]
            [services.helpers :as helpers]
            [shared.specs.core :as specs]
            [services.logger :as logger]))

(defmulti missing-data (fn [state {:keys [viewmodel]}]
                         (helpers/resolve-data-type ::specs/viewmodel viewmodel)))

(defmethod missing-data :resources [state {:keys [resources] :as viewmodel}]
  (let [state-urls  (into #{} (map :url (:resources state)))
        query-urls (into #{} (map :url resources))
        missing-urls (set/difference query-urls state-urls)
        missing-resources (map (fn [url] {:url url}) missing-urls)]
    (when-not (empty? missing-resources)
      (payload/new :resources missing-resources))))

(defmethod missing-data :collection [state {:keys [viewmodel]}]
  (query/create (:collection viewmodel)))

(defmethod missing-data :course [state proposal]
  (let [course (-> proposal :viewmodel :course)]
    (if (:checkpoints course)
      (va/missing-data state (payload/new :resources (map (fn [url] {:url url}) (qa/get course :urls {}))))
      (query/create course))))

(defmethod missing-data :checkpoint [state {:keys [course] :as viewmodel}]
  (payload/new :course course))

(defmethod missing-data :default [state proposal] nil)
