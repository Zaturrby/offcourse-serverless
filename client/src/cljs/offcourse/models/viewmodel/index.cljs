(ns offcourse.models.viewmodel.index
  (:require [protocols.convertible.index :refer [Convertible]]
            [offcourse.models.viewmodel.to-url :refer [to-url]]
            [specs.core :as specs]))

(defrecord Viewmodel []
  Convertible
  (-to-url [this routes] (to-url this routes)))

(defn -new [vm] (with-meta (map->Viewmodel vm) {:spec ::specs/viewmodel}))

(defmulti new (fn [type result] type))

(defmethod new :checkpoint-view [result]
  (-new {:course     (select-keys result [:curator :course-slug])
         :checkpoint (select-keys result [:checkpoint-slug :checkpoint-id])}))

(defmethod new :collection-view [type collection]
  (-new {:collection collection}))

(defmethod new :course-view [course]
  (-new {:course (select-keys course [:curator :course-slug])}))

(defmethod new :signup-view [user]
  (-new {:new-user user}))

(defmethod new :new-course-view [{:keys [new-course new-checkpoint]}]
  (-new {:new-course     new-course
         :new-checkpoint new-checkpoint}))

(defmethod new :home-view []
  (-new {:collection {:collection-type "flags"
                      :collection-name "featured"}}))
