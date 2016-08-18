(ns shared.models.checkpoint.index
  (:require [specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]))

(defrecord Checkpoint [])

(defn create [raw-checkpoint]
  (-> raw-checkpoint
      map->Checkpoint
      (with-meta {:spec ::specs/checkpoint})))
