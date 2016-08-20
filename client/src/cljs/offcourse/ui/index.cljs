(ns offcourse.ui.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [medley.core :as medley]
            [offcourse.models.view :as view]
            [offcourse.protocols.composable :as ca]
            [offcourse.protocols.mountable :as ma]
            [offcourse.protocols.renderable :as rr :refer [Renderable]]
            [shared.protocols.responsive :as ri :refer [Responsive]]
            [schema.core :as schema]
            [services.logger :as logger]))

(defrecord UI [component-name reactions]
  Lifecycle
  (start [rd] (ri/listen rd))
  (stop [rd] (ri/mute rd))
  Renderable
  (-render [{:keys [views url-helpers components handlers] :as rd}
            [_ payload]]
    (let [responder (partial ri/respond rd)
          handlers (medley/map-vals #(% responder) handlers)
          view     (view/new payload components url-helpers handlers)]
      (-> view
          (ca/compose views)
          (rr/render)
          (ma/mount "#container"))
      (ri/respond rd [:rendered nil])))
  Responsive
  (-listen [rd] (ri/listen rd))
  (-react [rd event] (rr/render rd event))
  (-mute [rd] (dissoc rd :listener))
  (-respond [rd event] (ri/respond rd event)))

(defn create [component-name reactions]
  (->UI component-name reactions))
