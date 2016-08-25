(ns shared.models.appstate.index
  (:refer-clojure :exclude [get -reset remove])
  (:require [shared.specs.core :as specs]
            [shared.protocols.actionable :refer [Actionable]]
            [shared.protocols.queryable :refer [Queryable]]
            [shared.models.appstate.missing-data :as md]
            [shared.models.appstate.get :as get]
            [shared.models.appstate.perform :as perform]))

(defrecord Appstate []
  Actionable
  (-perform [as action] (perform/perform as action))
  Queryable
  (-get [as query] (get/get as query))
  (-missing-data [as query] (md/missing-data as query)))

(defn create [appstate]
  (-> appstate
      map->Appstate
      (with-meta {:spec ::specs/appstate})))
