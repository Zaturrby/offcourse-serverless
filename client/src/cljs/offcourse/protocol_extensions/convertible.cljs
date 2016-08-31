(ns offcourse.protocol-extensions.convertible
  (:require [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.models.viewmodel.index :as viewmodel]
            [services.logger :as logger]))

(extend-protocol Convertible
  Course
  (-to-url [course routes]
    (let [viewmodel (viewmodel/create :course-view course)]
      (cv/to-url viewmodel routes))))
