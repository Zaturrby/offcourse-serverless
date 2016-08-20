(ns offcourse.api.index
  (:require [com.stuartsierra.component :as lc :refer [Lifecycle]]
            [offcourse.api.queryable :as qa-impl]
            [offcourse.protocols.queryable :as qa :refer [Queryable]]
            [offcourse.protocols.responsive :as ri :refer [Responsive]]))

(defn connect-to-repository [{:keys [adapter] :as config}]
  (lc/start (adapter (select-keys config [:name :endpoint :resources]))))

(defrecord API []
  Lifecycle
  (start [api]
    (-> api
        (update :repositories #(map connect-to-repository %))
        ri/listen))
  (stop [api] (ri/mute api))
  Queryable
  (-fetch [api event] (qa-impl/fetch api event))
  Responsive
  (-respond [api event] (ri/respond api event))
  (-mute [api] (ri/mute api))
  (-listen [api] (ri/listen api)))

(defn new []
  (map->API {:component-name :api
             :reactions {:not-found qa/fetch
                         :refreshed-auth-token qa/fetch}}))
