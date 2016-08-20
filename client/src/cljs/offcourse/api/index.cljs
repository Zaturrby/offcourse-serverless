(ns offcourse.api.index
  (:require [com.stuartsierra.component :as lc :refer [Lifecycle]]
            [offcourse.api.react :as re]
            [shared.protocols.responsive :as ri :refer [Responsive]]))

(defn connect-to-repository [{:keys [adapter] :as config}]
  (lc/start (adapter (select-keys config [:name :endpoint :resources]))))

(defrecord API [component-name reactions]
  Lifecycle
  (start [api]
    (-> api
        (update :repositories #(map connect-to-repository %))
        ri/listen))
  (stop [api] (ri/mute api))
  Responsive
  (-respond [api event] (ri/respond api event))
  (-react [api event] (re/react api event))
  (-mute [api] (ri/mute api))
  (-listen [api] (ri/listen api)))

(defn create [component-name reactions] (->API component-name reactions))
