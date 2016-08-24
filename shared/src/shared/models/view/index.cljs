(ns shared.models.view.index
  (:require [plumbing.graph :as graph]
            [rum.core :as rum]
            [shared.protocols.renderable :as rr :refer [Renderable]]
            [shared.protocols.validatable :as va]))

(defrecord View []
  Renderable
  (-render [view views element]
    (let [view-type (-> view :appstate :viewmodel va/resolve-type)
          view-graph (graph/compile (view-type views))
          {:keys [container] :as composition} (view-graph view)
          rendered-view (container composition)]
      (rum/mount rendered-view
                 (. js/document (querySelector element))))))

(defn new [appstate components url-helpers handlers]
  (map->View {:appstate    appstate
              :components  components
              :handlers    handlers
              :url-helpers url-helpers}))
