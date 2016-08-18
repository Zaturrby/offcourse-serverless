(ns shared.models.course.index
  (:require [specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [shared.models.course.search :as sr]
            [shared.models.checkpoint.index :as checkpoint]))

(defrecord Course []
  Queryable
  (-search [this query]
    (sr/search this query)))

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
