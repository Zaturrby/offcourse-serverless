(ns offcourse.protocols.decoratable
  (:require [offcourse.models.label :as lb]
            [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.protocols.queryable :as qa]
            [shared.protocols.validatable :as va]))

(defprotocol Decoratable
  (-decorate [this] [this appstate] [this user-name slug]))

(defn decorate
  ([this] (-decorate this))
  ([this appstate] (-decorate this appstate))
  ([this user-name slug] (-decorate this user-name slug)))

(defn select-checkpoint [checkpoints selected-slug]
  (for [{:keys [checkpoint-slug] :as checkpoint} checkpoints]
    (if (= selected-slug checkpoint-slug)
      (with-meta checkpoint {:selected true})
      checkpoint)))

(extend-protocol Decoratable
  Checkpoint
  (-decorate [{:keys [url] :as checkpoint} appstate]
    (let [resource (qa/search appstate {:url url})]
      (-> checkpoint
          (assoc :resource resource)
          (with-meta {:selected true}))))
  Course
  (-decorate
    ([{:keys [checkpoints] :as course}]
     (let [tags (-> (qa/search course {:tags :all})
                    (lb/collection->labels 0))
           valid? (va/valid? (co/complete course))
           saved? (:saved? (meta course))]
       (-> course (with-meta {:tags tags
                              :valid? valid?
                              :saved? saved?}))))
    ([{:keys [checkpoints curator] :as course} user-name selected]
     (let [tags (-> (qa/search course {:tags :all})
                    (lb/collection->labels selected))]
       (some-> course
               (assoc :checkpoints (select-checkpoint checkpoints selected))
               (with-meta {:tags tags
                           :trackable? (= user-name curator)}))))))
