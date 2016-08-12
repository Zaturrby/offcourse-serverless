(ns offcourse.models.payload.index
  (:require [offcourse.protocols.convertible :refer [Convertible]]
            [offcourse.models.payload.to-url :refer [to-url]]))

(defrecord Payload []
  Convertible
  (to-url [this routes] (to-url this routes)))

(defmulti new (fn [type result] type))

(defmethod new :permissions [type result]
  (map->Payload {:proposal result}))

(defmethod new :checkpoint-view [result]
   (map->Payload {:course     (select-keys result [:curator :course-slug])
                  :checkpoint (select-keys result [:checkpoint-slug :checkpoint-id])}))

(defmethod new :home-view []
  (map->Payload {:collection {:collection-type "flags"
                              :collection-name "featured"}}))

(defmethod new :collection-view [collection]
  (map->Payload {:collection collection}))

(defmethod new :course-view [course]
  (map->Payload {:course (select-keys course [:curator :course-slug])}))

(defmethod new :signup-view [user]
  (map->Payload {:new-user user}))

(defmethod new :new-course-view [{:keys [new-course new-checkpoint]}]
  (map->Payload {:new-course     new-course
                 :new-checkpoint new-checkpoint}))

(defmethod new :default [type result]
  (map->Payload {type result}))

