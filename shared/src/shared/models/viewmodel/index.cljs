(ns shared.models.viewmodel.index
  (:require [shared.protocols.convertible :refer [Convertible]]
            [shared.models.viewmodel.to-url :refer [to-url]]
            [shared.specs.core :as specs]
            [services.logger :as logger]))

(defrecord Viewmodel []
  Convertible
  (-to-url [this routes] (to-url this routes)))

(defn -create [vm] (with-meta (map->Viewmodel vm) {:spec ::specs/viewmodel}))

(defmulti create (fn [type result] type))

(defmethod create :checkpoint-view [type result]
  (-create {:course     (select-keys result [:curator :course-slug])
            :checkpoint (select-keys result [:checkpoint-slug :checkpoint-id])}))

(defmethod create :collection-view [type collection]
  (-create {:collection collection}))

(defmethod create :course-view [type course]
  (-create {:course (select-keys course [:curator :course-slug])}))

(defmethod create :signup-view [type]
  (-create {:user {}}))

(defmethod create :home-view []
  (-create {:collection {:collection-type "flags"
                         :collection-name "featured"}}))

