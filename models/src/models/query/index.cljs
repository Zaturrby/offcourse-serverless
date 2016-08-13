(ns models.query.index
  (:require [protocols.validatable :refer [Validatable]]
            [specs.core :as specs]))

(defrecord Query [])

(defn new [query]
  (with-meta (map->Query query) {:spec ::specs/query}))
