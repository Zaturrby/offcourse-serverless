(ns offcourse.ui.render
  (:require [medley.core :as medley]
            [shared.models.view.index :as view]
            [shared.protocols.renderable :as rr]
            [shared.protocols.responsive :as ri]
            [services.logger :as logger]
            [rum.core :as rum]
            [shared.protocols.validatable :as va]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]))

(defn -render [view-graph element]
  (let [{:keys [container] :as composition} view-graph
        rendered-view (container composition)]
    (rum/mount rendered-view
               (. js/document (querySelector element)))))

(defmulti render (fn [_ [_ payload]] (first (spec/conform ::specs/payload payload))))

(defmethod render :appstate [{:keys [views container url-helpers components] :as rd}
                             [_ payload]]
  (let [view       (view/create {:appstate    payload
                                 :responder   (partial ri/respond rd)
                                 :components  components
                                 :url-helpers url-helpers})
        view-graph (rr/render view views)
        viewmodel  (:viewmodel view-graph)
        actions    (:actions view-graph)]
    (-render view-graph container)
    (ri/respond rd [:rendered (assoc viewmodel :actions actions)])))

(defmethod render :default [_ _] nil)
