(ns shared.models.course.index
  (:refer-clojure :exclude [get -reset remove])
  (:require [specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [clojure.set :as set]
            [shared.protocols.validatable :as va]))

(defmulti get (fn [_ query] (va/resolve-type query)))

(defmethod get :tags [course _]
  (->> course
       :checkpoints
       (map :tags)
       (apply set/union)
       (into #{})))

(defrecord Course []
  Queryable
  (-get [this query]
    (get this query)))

(defn create [raw-course]
  (-> raw-course
      map->Course
      (with-meta {:spec ::specs/course})))

(defn complete [course]
  course)
