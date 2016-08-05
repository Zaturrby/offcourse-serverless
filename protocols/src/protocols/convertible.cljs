(ns protocols.convertible
  (:require [services.helpers :as helpers]))

(defprotocol Convertible
  (-to-event [this])
  (-to-action [this])
  (-to-payload [this]))

(defn to-event [this]
  (-to-event this))

(defn to-action [this]
  (-to-action this))

(defn to-payload [this]
  (-to-payload this))


