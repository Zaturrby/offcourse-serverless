(ns models.event.index
  (:require [services.logger :as logger]
            [protocols.convertible :refer [Convertible]]
            [models.event.to-payload :refer [to-payload]]
            [models.event.to-query :refer [to-query]]
            [models.event.to-action :refer [to-action]]))

(defrecord Event []
  Convertible
  (-to-action [this] (to-action this))
  (-to-payload [this] (to-payload this))
  (-to-query [this] (to-query this)))
