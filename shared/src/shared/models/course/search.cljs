(ns shared.models.course.search
  (:require [specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [clojure.set :as set]
            [shared.protocols.validatable :as va]))

(defmulti search (fn [_ query] (va/resolve-type query)))

(defmethod search :tags [course _]
  (->> course
       :checkpoints
       (map :tags)
       (apply set/union)
       (into #{})))

(defmethod search :urls [course _]
  (->> course
       :checkpoints
       (map :url)
       (into #{})))
