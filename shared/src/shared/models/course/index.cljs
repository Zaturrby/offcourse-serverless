(ns shared.models.course.index
  (:refer-clojure :exclude [get -reset remove])
  (:require [shared.specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [shared.models.course.get :as get]
            [shared.models.checkpoint.index :as checkpoint]
            [cljs.spec :as spec]))


(defrecord Course []
  Queryable
  (-get [this query] (get/get this query)))

(defn add-checkpoints [{:keys [checkpoints] :as course}]
  (let [checkpoints (map checkpoint/create checkpoints)]
    (assoc course :checkpoints checkpoints)))

(defn order-checkpoints [{:keys [checkpoints] :as course}]
  (let [checkpoints (map-indexed #(assoc %2 :checkpoint-id %1) checkpoints)]
    (assoc course :checkpoints checkpoints)))

(defn create [raw-course]
  (-> raw-course
      map->Course
      add-checkpoints
      (with-meta {:spec ::specs/course})))


(defn add-id [{:keys [organization curator] :as course}]
  (let [hash (hash course)
        id (str organization "::" curator "::" hash)]
    (assoc course
           :base-id id
           :course-id id)))

(defn add-meta [course]
  (assoc course
         :flags ["featured"]
         :version [0 0 1]
         :revision 1
         :forks []
         :forked-from nil))

(defn initialize [raw-course]
  (->> raw-course
       create
       order-checkpoints
       add-id
       add-meta))
