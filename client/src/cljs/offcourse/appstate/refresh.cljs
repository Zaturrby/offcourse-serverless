(ns offcourse.appstate.refresh
  (:require [offcourse.models.course.index :as co]
            [offcourse.protocols.queryable :as qa]
            [shared.protocols.queryable :as qa2]
            [offcourse.protocols.redirectable :as rd]
            [offcourse.protocols.responsive :as ri]
            [shared.protocols.validatable :as va]
            [shared.models.data-payload.index :as data-payload]
            [shared.models.query.index :as query]
            [services.logger :as logger]
            [shared.protocols.commandable :as cd]))

(defmulti refresh (fn [_ {:keys [type]}] type))

(defmethod refresh :requested-update [{:keys [state] :as as} {:keys [payload] :as query}]
  (let [proposal (cd/exec @state payload)]
    (when (qa/check as :proposal proposal)
      (reset! state proposal)
      (ri/respond as :refreshed-state :appstate @state))))

(defmethod refresh :requested-save-course [{:keys [state] :as as} _]
  (let [course (-> @state :viewmodel :new-course co/complete)
        proposal (qa/add @state :course course)]
    (when (qa/check as :proposal proposal)
      (reset! state proposal)
      (rd/redirect as :course course))))

(defmethod refresh :requested-save-user [{:keys [state] :as as} _]
  (let [user (-> @state :viewmodel :new-user)
        proposal (qa/add @state {:type :user-profile
                                 :user-profile user})]
    (when (qa/check as :proposal proposal)
      (reset! state proposal)
      (rd/redirect as :home))))

(defmethod refresh :fetched-auth-token [{:keys [state] :as as} {:keys [payload] :as query}]
  (let [auth-token (:auth-token payload)
        proposal (cd/exec @state auth-token)]
    (when (and (qa/check as :proposal proposal) )
      (reset! state proposal)
      (ri/respond as :not-found-data {:type :user-profile
                                      :auth-token auth-token}))))

(defmethod refresh :removed-auth-token [{:keys [state] :as as} _]
  (let [proposal (cd/exec @state (:auth-token nil))]
    (when (qa/check as :proposal proposal)
      (reset! state proposal)
      (rd/redirect as :home))))

(defmethod refresh :requested-view [{:keys [state] :as as} {:keys [payload] :as query}]
  (let [proposal (cd/exec @state [:update payload])]
    (if (qa/check as :proposal proposal)
      (do
        (reset! state proposal)
        (when-let [missing-data (qa2/missing-data @state proposal)]
          (logger/log missing-data)
          (ri/respond as :not-found-data missing-data))
        (if (va/valid? @state)
          (ri/respond as :refreshed-state :appstate @state)
          (println "OHH SHIITTT")))
      (when (= (-> @state :viewmodel :type) :loading)
        (rd/redirect as :home)))))

(defmethod refresh :not-found-data [{:keys [state] :as as} {:keys [payload] :as query}]
  (when-not (-> @state :user :user-name)
    (rd/redirect as :signup)))

(defmethod refresh :found-data [{:keys [state] :as as} {:keys [payload] :as event}]
  (let [proposal (cd/exec @state [:add payload])]
    (when (va/valid? proposal)
      (reset! state proposal)
      (ri/respond as :refreshed-state :appstate @state))))
