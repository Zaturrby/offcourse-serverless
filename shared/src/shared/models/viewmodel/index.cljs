(ns shared.models.viewmodel.index
  (:require [shared.protocols.convertible :refer [Convertible]]
            [shared.models.viewmodel.to-url :refer [to-url]]
            [shared.specs.core :as specs]))

(defrecord Viewmodel []
  Convertible
  (-to-url [this routes] (to-url this routes)))

(defn -create [vm]
  (with-meta (map->Viewmodel vm) {:spec ::specs/viewmodel}))

(defmulti create (fn [type result] type))


(defmethod create :checkpoint-org-view [type result]
  (-create {:course     (select-keys result [:curator :organization :course-slug])
            :checkpoint (select-keys result [:checkpoint-slug :checkpoint-id])}))


(defmethod create :checkpoint-view [type result]
  (-create {:course     (select-keys result [:curator :organization :course-slug])
            :checkpoint (select-keys result [:checkpoint-slug :checkpoint-id])}))

(defmethod create :collection-view [type collection]
  (-create {:collection collection}))

(defmethod create :course-view [type result]
  (-create {:course (select-keys result [:curator :organization :course-slug])}))

(defmethod create :course-org-view [type result]
  (-create {:course (select-keys result [:curator :organization :course-slug])}))

(defmethod create :signup-view [type]
  (-create {:user {}}))

(defmethod create :home-view []
  (-create {:collection {:collection-type "flags"
                         :collection-name "featured"}}))

