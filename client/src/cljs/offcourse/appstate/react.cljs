(ns offcourse.appstate.react
  (:require [offcourse.appstate.redirect :as rd]
            [shared.protocols.responsive :as ri]
            [offcourse.appstate.check :as ck]
            [shared.protocols.actionable :as ac]
            [shared.protocols.queryable :as qa]
            [shared.protocols.validatable :as va]
            [services.logger :as logger]))

(defmulti react (fn [_ event] (va/resolve-type event)))

(defmethod react [:granted :credentials] [{:keys [state] :as as} [_ payload]]
  (let [auth-token (:auth-token payload)
        proposal (ac/perform @state [:update payload])]
    (when (and (ck/check as proposal) )
      (reset! state proposal)
      (ri/respond as [:not-found {:user-profile nil}]))))

(defmethod react [:requested :viewmodel] [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:update payload])]
    (if (ck/check as proposal)
      (do
        (reset! state proposal)
        (when-let [missing-data (qa/missing-data @state proposal)]
          (ri/respond as [:not-found missing-data]))
        (if (va/valid? @state)
          (ri/respond as [:refreshed @state])
          (logger/log "Error" "OHH SHIITTT")))
      (when (= (-> @state :viewmodel va/resolve-type) :loading)
        (rd/redirect as :home)))))

(defmethod react [:found :data] [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (when (va/valid? proposal)
      (reset! state proposal)
      (ri/respond as [:refreshed @state]))))

(defmethod react [:not-found :data] [{:keys [state] :as as} [_ payload]]
  (logger/log "DATA NOT FOUND" payload)
  #_(when-not (-> @state :user :user-name)
      (rd/redirect as :signup)))
