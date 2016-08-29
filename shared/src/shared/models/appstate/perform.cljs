(ns shared.models.appstate.perform
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

(defmulti perform (fn [as action] (va/resolve-type action)))

(defmethod perform [:update :viewmodel] [state [_ viewmodel]]
  (-> state (assoc :viewmodel viewmodel)))

(defmethod perform [:add :view-actions] [{:keys [viewmodel] :as state} [_ view-actions]]
  (-> state (assoc :viewmodel (assoc viewmodel :actions (second view-actions)))))

(defmethod perform [:update :credentials] [state [_ {:keys [auth-token]}]]
  (-> state
      (assoc :auth-token auth-token)
      (assoc :user nil)))

(defmethod perform [:add :courses] [store [_ courses]]
  (reduce add-course store courses))

(defmethod perform [:add :course] [store [_ course]]
  (add-course store course))
