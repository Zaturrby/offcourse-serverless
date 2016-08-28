(ns shared.models.course.index
  (:refer-clojure :exclude [get -reset remove])
  (:require [shared.specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [shared.models.course.get :as get]
            [shared.models.checkpoint.index :as checkpoint]))


(defrecord Course []
  Queryable
  (-get [this query] (get/get this query)))

(defn add-checkpoints [{:keys [checkpoints] :as course}]
  (let [checkpoints (map checkpoint/create checkpoints)]
    (assoc course :checkpoints checkpoints)))

(defn create [raw-course]
  (-> raw-course
      map->Course
      add-checkpoints
      (with-meta {:spec ::specs/course})))

(defn complete [course]
  course)
