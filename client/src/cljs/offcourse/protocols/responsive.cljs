(ns offcourse.protocols.responsive
  (:require [cljs.core.async :as async :refer [<! put! close!]]
            [services.logger :as logger]
            [shared.models.event.index :as event]
            [shared.protocols.validatable :as va])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defprotocol Responsive
  (-listen [this])
  (-mute [this])
  (-react [this event])
  (-respond [this event]))

(defn respond
  "Puts an event on the output channel of a component"
  ([{:keys [channels component-name] :as this} [status payload]]
   (let [response (event/create [component-name status payload])]
     (if (va/valid? response)
       (async/put! (:output channels) response)
       (logger/log (va/errors response))))))

(defn react
  "Has a component react to an event based on the event's specification type"
  [this event]
  (-react this event))

(defn- listener [{:keys [channels component-name reactions] :as this}]
  (go-loop []
    (let [[type :as event] (<! (:input channels))]
      (when (contains? (into #{} reactions) type) (react this event))
      (recur))))

(defn listen
  "Has a component listen for events on the input channel, and reacts when the event is relevant"
  [{:keys [channels component-name reactions] :as this}]
  (assoc this :listener (listener this)))

(defn mute
  "Makes a component stop listening for events"
  [{:keys [channels] :as this}]
  (close! (:input channels))
  (-> this
      (dissoc :listener)))
