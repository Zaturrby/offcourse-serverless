(ns offcourse.router.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.protocols.responsive :as ri :refer [Responsive]]
            [offcourse.router.react :as react-impl]
            [offcourse.router.responsive :as ri-impl]))

(defrecord Router [component-name reactions]
  Lifecycle
  (start [rt] (ri/-listen rt))
  (stop [rt] (ri/mute rt))
  Responsive
  (-listen [rt] (ri-impl/listen rt))
  (-react [rt event] (react-impl/refresh rt event))
  (-mute [rt] (ri-impl/mute rt))
  (-respond [rt event] nil))

(defn create [component-name reactions] (->Router component-name reactions))
