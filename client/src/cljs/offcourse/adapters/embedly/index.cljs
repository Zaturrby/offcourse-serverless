(ns offcourse.adapters.embedly.index
(:require [com.stuartsierra.component :refer [Lifecycle]]
          [offcourse.adapters.embedly.queryable :as qa-impl]
          [shared.protocols.responsive :refer [Responsive]]))

(defrecord Embedly [name supported-types connection]
  Lifecycle
  (start [db] db)
  (stop  [db] db)
  Responsive
  (-send   [db query] (qa-impl/fetch db query)))


(defn new-db [config]
  (map->Embedly config))
