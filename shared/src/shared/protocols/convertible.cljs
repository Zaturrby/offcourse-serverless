(ns shared.protocols.convertible
  (:require [services.helpers :as helpers]))

(defprotocol Convertible
  (-to-event [this])
  (-to-action [this])
  (-to-query [this])
  (-to-payload [this])
  (-to-models [this])
  (-to-data-payload [this])
  (-to-url [this routes]))

(defn to-event
  "Convert an object to an event, if the object meets the event specification"
  [this]
  (-to-event this))

(defn to-action
  "Converts an object to an action, if the object meets the action specification"
  [this]
  (-to-action this))

(defn to-payload
  "Converts an object to a payload, if the object meets the payload specification"
  [this]
  (-to-payload this))

(defn to-data-payload
  "Converts an object to a data payload, if the object meets the data payload specification"
  [this]
  (-to-data-payload this))

(defn to-query
  "Converts an object to a query, if the object meets the query specification"
  [this]
  (-to-query this))

(defn to-models
  "Converts an object to offcourse models, if the object meets the corresponding model specification"
  [this]
  (-to-models this))

(defn to-url
  "Converts an object and a set of routes to a url, when the object implements this particular protocol"
  [this routes]
  (-to-url this routes))
