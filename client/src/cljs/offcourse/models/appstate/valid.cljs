(ns offcourse.models.appstate.valid
  (:require [offcourse.protocols.queryable :as qa]
            [cljs.spec :as spec]
            [specs.core :as vm-specs]
            [offcourse.specs.appstate :as specs]))

(defmulti valid? (fn [{:keys [viewmodel]}] (first (spec/conform ::vm-specs/viewmodel viewmodel))))

(defmethod valid? :collection [as as-schema]
  (println (spec/explain ::specs/appstate as))
  (spec/valid? ::specs/appstate as))

(defmethod valid? :course [as as-schema]
  (spec/valid? ::specs/appstate as))

(defmethod valid? :checkpoint [as as-schema]
  (spec/valid? ::specs/appstate as))

(defmethod valid? :new-course [as as-schema]
  true)

(defmethod valid? :signup [{:keys [auth-token]} as-schema]
  (if auth-token true false))

(defmethod valid? :default [as as-schema]
  false)
