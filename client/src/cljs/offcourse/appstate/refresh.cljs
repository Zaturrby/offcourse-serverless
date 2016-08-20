(ns offcourse.appstate.refresh
  (:require [offcourse.protocols.queryable :as qa]
            [offcourse.protocols.redirectable :as rd]
            [offcourse.protocols.responsive :as ri]
            [shared.protocols.commandable :as cd]
            [shared.protocols.queryable :as qa2]
            [shared.protocols.validatable :as va]))

(defmulti refresh (fn [_ event] (va/resolve-type event)))

(defmethod refresh [:found :credentials] [{:keys [state] :as as} [_ payload]]
  (let [auth-token (:auth-token payload)
        proposal (cd/exec @state [:update payload])]
    (when (and (qa/check as proposal) )
      (reset! state proposal)
      (ri/respond as [:not-found {:user-profile nil}]))))

(defmethod refresh [:requested :viewmodel] [{:keys [state] :as as} [_ payload]]
  (let [proposal (cd/exec @state [:update payload])]
    (if (qa/check as proposal)
      (do
        (reset! state proposal)
        (when-let [missing-data (qa2/missing-data @state proposal)]
          (ri/respond as [:not-found missing-data]))
        (if (va/valid? @state)
          (ri/respond as [:refreshed @state])
          (println "OHH SHIITTT")))
      (when (= (-> @state :viewmodel va/resolve-type) :loading)
        (rd/redirect as :home)))))

(defmethod refresh [:found :data] [{:keys [state] :as as} [_ payload]]
  (let [proposal (cd/exec @state [:add payload])]
    (when (va/valid? proposal)
      (reset! state proposal)
      (ri/respond as [:refreshed @state]))))

(defmethod refresh [:not-found :data] [{:keys [state] :as as} [_ payload]]
  #_(when-not (-> @state :user :user-name)
    (rd/redirect as :signup)))
