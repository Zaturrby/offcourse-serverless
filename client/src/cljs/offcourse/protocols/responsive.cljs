(ns offcourse.protocols.responsive
  (:require [cljs.core.async :refer [<! >! close!]]
            [offcourse.models.action :as action]
            [offcourse.models.payload.index :as payload :refer [Payload]]
            [services.logger :as logger]
            [shared.models.event.index :as event]
            [shared.protocols.convertible :as cv])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defprotocol Responsive
  (-listen [this])
  (-mute [this])
  (-respond [this status] [this status payload] [this status type result]))

(defn debug-helper [component-name status payload]
  (when true #_(= component-name :appstate)
    (println "--RESPONSE-----")
    (println "SENDER" component-name)
    (println "STATUS" status)
    (println "PAYLOAD" payload)))

(def counter (atom 0))

(defn respond
  ([this {:keys [type payload]}] (respond this type payload))
  ([{:keys [output-channel channels component-name] :as this} status payload]
   (let [output-channel (or output-channel (:output channels))
         response             (event/create [status payload])]
     (when (< @counter 10)
       (go
         (swap! counter inc)
         (logger/log response)
         (>! output-channel response)))))
  ([this status type result]
   (-respond this status (payload/new type result))))


(defn -listener [{:keys [channels component-name reactions] :as this}]
  (go-loop []
    (let [[type payload :as event] (<! (:input channels))
          reaction (type reactions)]
      (when reaction
        (reaction this event))
      (recur))))

(defn listen [{:keys [channels component-name reactions] :as this}]
  (assoc this :listener (-listener this)))

(defn mute [{:keys [channels] :as this}]
  (close! (:input channels))
  (-> this
      (dissoc :listener)))
