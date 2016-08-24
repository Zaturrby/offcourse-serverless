(ns offcourse.ui.render
  (:require [medley.core :as medley]
            [offcourse.models.view :as view]
            [offcourse.protocols.renderable :as rr]
            [offcourse.protocols.responsive :as ri]))

(defn render [{:keys [views container url-helpers components handlers] :as rd}
              [_ payload]]
  (let [responder (partial ri/respond rd)
        handlers (medley/map-vals #(% responder) handlers)
        view     (view/new payload components url-helpers handlers)]
    (rr/render view views container)
    (ri/respond rd [:rendered nil])))
