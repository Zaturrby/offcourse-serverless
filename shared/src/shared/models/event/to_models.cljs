(ns shared.models.event.to-models
  (:require [shared.models.course.index :as co]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]))

(defmulti to-models (fn [[_ payload]]
                      (first (spec/conform ::specs/payload payload))))

(defmethod to-models :courses [[_ payload]]
  (map co/create payload))

(defmethod to-models :new-courses [[_ payload]]
  (map co/initialize payload))

(defmethod to-models :new-course [[_ payload]]
  (co/initialize payload))

(defmethod to-models :course [[_ payload]]
  (co/create (dissoc payload :course-slug)))
