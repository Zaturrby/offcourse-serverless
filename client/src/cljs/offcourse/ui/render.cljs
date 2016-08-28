(ns offcourse.ui.render
  (:require [medley.core :as medley]
            [shared.models.view.index :as view]
            [shared.protocols.renderable :as rr]
            [shared.protocols.responsive :as ri]))

(defn render [{:keys [views container url-helpers components handlers] :as rd}
              [_ payload]]
  (let [view      (view/create {:appstate    payload
                                :responder   (partial ri/respond rd)
                                :components  components
                                :url-helpers url-helpers})]
    (rr/render view views container)
    (ri/respond rd [:rendered nil])))
