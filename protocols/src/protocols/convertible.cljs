(ns protocols.convertible)

(defprotocol Convertible
  (-to-action [this]))

(defn to-action [this]
  (-to-action this))
