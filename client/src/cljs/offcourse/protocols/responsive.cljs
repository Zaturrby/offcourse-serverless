(ns offcourse.protocols.responsive
  (:require [cljs.core.async :as async :refer [<! put! close!]]
            [services.logger :as logger]
            [shared.models.event.index :as event]
            [shared.protocols.validatable :as va])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defprotocol Responsive
  (-listen [this])
  (-mute [this])
  (-respond [this event]))

(defn respond
  ([{:keys [channels component-name] :as this} [status payload]]
   (let [response (event/create [component-name status payload])]
     (if (va/valid? response)
       (async/put! (:output channels) response)
       (logger/log (va/errors response))))))

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
