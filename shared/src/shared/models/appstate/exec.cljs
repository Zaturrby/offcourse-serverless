(ns shared.models.appstate.exec
  (:require [shared.protocols.validatable :as va]
            [shared.protocols.queryable :as qa]
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

(defmulti exec (fn [as [command-type :as command]] (va/resolve-type command)))

(defmethod exec [:update :viewmodel] [state [_ viewmodel]]
  (-> state (assoc :viewmodel viewmodel)))

(defmethod exec [:update :credentials] [state [_ {:keys [auth-token]}]]
  (-> state
      (assoc :auth-token auth-token)
      (assoc :user nil)))

(defmethod exec [:add :courses] [store [_ courses]]
  (reduce add-course store courses))

(defmethod exec [:add :course] [store [_ course]]
  (add-course store course))
