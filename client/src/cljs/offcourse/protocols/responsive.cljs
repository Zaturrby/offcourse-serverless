(ns offcourse.protocols.responsive
  (:require [cljs.core.async :refer [<! >! close!]]
            [offcourse.models.action :as action]
            [offcourse.models.payload.index :as payload :refer [Payload]])
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
         response       (action/new this status payload)]
     (println component-name status payload)
     (when (< @counter 10)
       (go
         (swap! counter inc)
         (println @counter)
         (>! output-channel response)))))
  ([this status type result]
   (-respond this status (payload/new type result))))


(defn -listener [{:keys [channels component-name reactions] :as this}]
  (go-loop []
    (let [{:keys [type source payload] :as action} (<! (:input channels))
          reaction (type reactions)]
      #_(println component-name source type payload)
      (when reaction
        (reaction this action))
      (recur))))

(defn listen [{:keys [channels component-name reactions] :as this}]
  (assoc this :listener (-listener this)))

(defn mute [{:keys [channels] :as this}]
  (close! (:input channels))
  (-> this
      (dissoc :listener)))
