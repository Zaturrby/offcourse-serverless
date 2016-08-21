(ns offcourse.adapters.fakedb.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.adapters.fakedb.fetch :refer [fetch]]
            [offcourse.protocols.queryable :refer [Queryable]]))

(defrecord FakeDB [name supported-types connection]
  Lifecycle
  (start [db] db)
  (stop  [db] db)
  Queryable
  (-fetch   [db query] (fetch db query)))

(defn new-db [config]
  (map->FakeDB config))
