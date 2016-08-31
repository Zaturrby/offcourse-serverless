(ns offcourse.protocol-extensions.decoratable
  (:require [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.protocols.decoratable :as dc :refer [Decoratable]]
            [shared.protocols.queryable :as qa]
            [shared.protocols.convertible :as cv]
            [services.logger :as logger]))

(extend-protocol Decoratable
  Checkpoint
  (-decorate [{:keys [checkpoint-slug] :as checkpoint} selected-slug course routes]
    (let [checkpoint-url (cv/to-url checkpoint course routes)]
      (if (= selected-slug checkpoint-slug)
        (with-meta checkpoint {:selected true
                               :checkpoint-url checkpoint-url})
        (with-meta checkpoint {:checkpoint-url checkpoint-url}))))
  Course
  (-decorate [{:keys [checkpoints curator] :as course} user-name selected routes]
    (let [tags (-> (qa/get course {:tags :all}))
          course-url (cv/to-url course routes)]
      (some-> course
              (assoc :checkpoints (map #(dc/decorate %1 (:checkpoint-slug selected) course routes) checkpoints))
              (with-meta {:tags       tags
                          :course-url course-url
                          :trackable? (= user-name curator)})))))
