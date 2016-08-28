(ns shared.models.view.index
  (:require [plumbing.graph :as graph]
            [rum.core :as rum]
            [shared.protocols.renderable :as rr :refer [Renderable]]
            [shared.protocols.validatable :as va]
            [services.logger :as logger]))

(defrecord View []
  Renderable
  (-render [view views element]
    (let [view-type (-> view :appstate :viewmodel va/resolve-type)
          view-graph (graph/compile (view-type views))
          {:keys [container] :as composition} (view-graph view)
          rendered-view (container composition)]
      (rum/mount rendered-view
                 (. js/document (querySelector element))))))

(defn create [view-data]
  (map->View view-data))
