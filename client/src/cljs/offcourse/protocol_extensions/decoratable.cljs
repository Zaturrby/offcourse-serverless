(ns offcourse.protocol-extensions.decoratable
  (:require [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.protocols.decoratable :refer [Decoratable]]
            [shared.protocols.queryable :as qa]
            [shared.protocols.convertible :as cv]))


(defn select-checkpoint [checkpoints selected-slug]
  (for [{:keys [checkpoint-slug] :as checkpoint} checkpoints]
    (if (= selected-slug checkpoint-slug)
      (with-meta checkpoint {:selected true})
      checkpoint)))

(extend-protocol Decoratable
  Checkpoint
  (-decorate [{:keys [url] :as checkpoint} appstate]
    (let [resource (qa/get appstate {:url url})]
      (-> checkpoint
          (assoc :resource resource)
          (with-meta {:selected true}))))
  Course
  (-decorate [{:keys [checkpoints curator] :as course} user-name selected routes]
    (let [tags (-> (qa/get course {:tags :all}))
          course-url (cv/to-url course routes)]
      (some-> course
              (assoc :checkpoints (select-checkpoint checkpoints (:checkpoint-slug selected)))
              (with-meta {:tags       tags
                          :course-url course-url
                          :trackable? (= user-name curator)})))))