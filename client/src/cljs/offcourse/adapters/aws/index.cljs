(ns offcourse.adapters.aws.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.adapters.aws.send :as send-impl]
            [shared.protocols.responsive :as ri :refer [Responsive]]))

(defrecord AWS [name supported-types connection]
  Lifecycle
  (start [db] db)
  (stop  [db] db)
  Responsive
  (-send [db event] (send-impl/send db event)))

(defn new-db [config]
  (map->AWS config))
