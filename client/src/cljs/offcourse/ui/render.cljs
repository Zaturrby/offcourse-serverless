(ns offcourse.ui.render
  (:require [medley.core :as medley]
            [offcourse.models.view :as view]
            [offcourse.protocols.composable :as ca]
            [offcourse.protocols.mountable :as ma]
            [offcourse.protocols.renderable :as rr]
            [offcourse.protocols.responsive :as ri]))

(defn render [{:keys [views url-helpers components handlers] :as rd}
              [_ payload]]
  (let [responder (partial ri/respond rd)
        handlers (medley/map-vals #(% responder) handlers)
        view     (view/new payload components url-helpers handlers)]
    (-> view
        (ca/compose views)
        (rr/render)
        (ma/mount "#container"))
    (ri/respond rd [:rendered nil])))
