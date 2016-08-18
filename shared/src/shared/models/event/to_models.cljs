(ns shared.models.event.to-models
  (:require [shared.models.course.index :as co]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]))

(defmulti to-models (fn [event]
                      (first (spec/conform ::specs/data-payload (:payload event)))))

(defmethod to-models :courses [event]
  (map co/create (:payload event)))

(defmethod to-models :course [event]
  (co/create (:payload event)))
