(ns shared.models.checkpoint.index
  (:require [shared.specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]))

(defrecord Checkpoint [])

(defn create [raw-checkpoint]
  "creates a checkpoint"
  (-> raw-checkpoint
      map->Checkpoint
      (with-meta {:spec ::specs/checkpoint})))
