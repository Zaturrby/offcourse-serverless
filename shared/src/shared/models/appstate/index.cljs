(ns shared.models.appstate.index
  (:require [shared.specs.core :as specs]
            [shared.protocols.commandable :refer [Commandable]]
            [shared.protocols.queryable :refer [Queryable]]
            [shared.models.appstate.missing-data :as md]
            [shared.models.appstate.search :as search]
            [shared.models.appstate.exec :as ex]))

(defrecord Appstate []
  Commandable
  (-exec [as command] (ex/exec as command))
  Queryable
  (-search [as query] (search/search as query))
  (-missing-data [as query] (md/missing-data as query)))

(defn create [appstate]
  (-> appstate
      map->Appstate
      (with-meta {:spec ::specs/appstate})))