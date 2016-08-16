(ns offcourse.protocols.decoratable
  (:require [shared.models.course.index :as co :refer [Course]]
            [offcourse.models.checkpoint.index :refer [Checkpoint]]
            [offcourse.models.profile.index :refer [Profile]]
            [offcourse.models.label :as lb]
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
  Profile
  (-decorate [profile]
    (let [valid? (va/valid? profile)]
      (-> profile (with-meta {:valid? valid?}))))
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
     (let [tags (-> (qa/get course {:tags :all})
                    (lb/collection->labels selected))]
       (some-> course
               (assoc :checkpoints (select-checkpoint checkpoints selected))
               (with-meta {:tags tags
                           :trackable? (= user-name curator)}))))))
