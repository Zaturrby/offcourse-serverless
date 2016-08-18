(ns offcourse.models.appstate.add
  (:require [shared.protocols.queryable :as qa]
            [offcourse.models.appstate.paths :as paths]
            [com.rpl.specter :refer [ALL select-first setval transform select-first]]
            [offcourse.models.course.index :as co]
            [offcourse.models.collection :as cl]
            [shared.protocols.validatable :as va]
            [offcourse.models.checkpoint.index :as cp]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [shared.models.query.index :as query]
            [services.logger :as logger]))

(defn- add-course [store course]
  (if-not (qa/get store (query/create course))
    (update-in store [:courses] #(conj % course))
    store))

(defn- add-resource [store resource]
  (if-not (qa/get store resource)
    (update-in store [:resources] #(conj % resource))
    store))

(defmulti add (fn [_ query] (va/resolve-type query)))

(defmethod add :courses [store courses]
  (reduce add-course store courses))

(defmethod add :course [store course]
  (add-course store course))

(defmethod add :resources [store {:keys [resources]}]
  (reduce add-resource store resources))

(defmethod add :resource [store {:keys [resource]}]
  (add-resource store resource))

(defmethod add :user-profile [store {:keys [user-profile] :as query}]
  (assoc store :user user-profile))

(defmethod add :default [_ _]
  {:type :error
   :error :query-not-supported})
