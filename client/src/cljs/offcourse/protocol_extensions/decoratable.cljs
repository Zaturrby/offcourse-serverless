(ns offcourse.protocol-extensions.decoratable
  (:require [shared.models.label.index :as lb]
            [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.protocols.decoratable :refer [Decoratable]]
            [shared.protocols.queryable :as qa]
            [shared.protocols.validatable :as va]
            [services.logger :as logger]))

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
  (-decorate
    ([{:keys [checkpoints] :as course}]
     (let [tags (-> (qa/get course {:tags :all})
                    (lb/collection->labels 0))
           valid? (va/valid? (co/complete course))
           saved? (:saved? (meta course))]
       (-> course (with-meta {:tags tags
                              :valid? valid?
                              :saved? saved?}))))
    ([{:keys [checkpoints curator] :as course} user-name selected]
     (let [tags (-> (qa/get course {:tags :all}))]
       (some-> course
               (assoc :checkpoints (select-checkpoint checkpoints (:checkpoint-slug selected)))
               (with-meta {:tags tags
                           :trackable? (= user-name curator)}))))))
