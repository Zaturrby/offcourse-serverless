(ns models.event.index
  (:require [services.logger :as logger]
            [protocols.convertible :refer [Convertible]]
            [models.event.convertible :as cv-impl]))

(defrecord Event []
  Convertible
  (-to-action [this] (cv-impl/to-action this)))
