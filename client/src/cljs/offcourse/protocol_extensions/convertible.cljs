(ns offcourse.protocol-extensions.convertible
  (:require [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.models.viewmodel.index :as viewmodel]
            [services.logger :as logger]
            [cuerdas.core :as str]))

(extend-protocol Convertible
  Checkpoint
  (-to-url [{:keys [task] :as cp} {:keys [goal organization curator] :as course} routes]
    (let [viewmodel (viewmodel/create :checkpoint-view {:checkpoint-slug (str/slugify task)
                                                        :organization organization
                                                        :course-slug (str/slugify goal)
                                                        :curator curator})]
      (cv/to-url viewmodel routes)))
  Course
  (-to-url [{:keys [curator organization goal]} routes]
    (let [viewmodel (viewmodel/create :course-view {:curator curator
                                                    :organization organization
                                                    :course-slug (str/slugify goal)})]
      (cv/to-url viewmodel routes))))
