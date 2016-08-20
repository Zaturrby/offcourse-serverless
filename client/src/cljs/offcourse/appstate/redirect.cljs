(ns offcourse.appstate.redirect
  (:require [offcourse.protocols.queryable :as qa]
            [shared.models.event.index :as event]
            [shared.models.viewmodel.index :as viewmodel]))

(defmulti redirect (fn [as destination & _] destination))

(defmethod redirect :home [{:keys [component-name] :as as} destination]
  (qa/refresh as
              (event/create [component-name :requested-view (viewmodel/create :home-view)])))

(defmethod redirect :signup [{:keys [component-name] :as as} destination]
  (qa/refresh as
              (event/create [component-name :requested-view (viewmodel/create :signup-view)])))

(defmethod redirect :course [{:keys [component-name] :as as} destination course]
  (qa/refresh as
              (event/create [component-name :requested-view (viewmodel/create :course-view course)])))
