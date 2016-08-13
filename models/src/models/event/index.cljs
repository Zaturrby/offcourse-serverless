(ns models.event.index
  (:require [models.event.to-action :refer [to-action]]
            [models.event.to-payload :refer [to-payload]]
            [models.event.to-query :refer [to-query]]
            [protocols.convertible.index :refer [Convertible]]
            [services.helpers :as helpers]
            [services.logger :as logger]))

(defrecord Event []
  Convertible
  (-to-action [this] (to-action this))
  (-to-payload [this] (to-payload this))
  (-to-query [this] (to-query this)))

(defn create [raw-event]
  (-> raw-event
      (js->clj :keywordize-keys true)
      helpers/keywordize-type
      map->Event))

(extend-protocol Convertible
  object
  (-to-event [raw] (create raw))
  PersistentArrayMap
  (-to-event [raw] (create raw)))
