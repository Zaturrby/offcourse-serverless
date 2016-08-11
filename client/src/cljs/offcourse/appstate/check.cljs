(ns offcourse.appstate.check
  (:require [cljs.spec :as spec]
            [specs.core :as specs]))

(defn viewmodel-type [{:keys [viewmodel] :as state}]
  (when viewmodel (first (spec/conform ::specs/viewmodel viewmodel))))

(defmulti check (fn [_ {:keys [type]}] type))

(defmethod check :permissions [{:keys [state] :as as} {:keys [payload] :as q}]
  (let [old-type (viewmodel-type @state)
        new-type(viewmodel-type payload)
        user-name (-> payload :user :user-name)
        auth-token (-> payload :auth-token)]
    (cond
      (and (= old-type :signup) (= new-type :signup)) true
      (and (= old-type :new-course) (= new-type :new-course)) true
      (and (= new-type :new-course) (not user-name)) false
      (and (= old-type :signup) (and auth-token (not user-name))) false
      :default true)))
