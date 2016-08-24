(ns offcourse.models.view
  (:require [offcourse.protocols.renderable :as rr :refer [Renderable]]
            [plumbing.graph :as graph]
            [rum.core :as rum]
            [shared.protocols.validatable :as va]))

(defrecord View []
  Renderable
  (-render [view views element]
    (let [viewmodel (-> view :appstate :viewmodel)
          view-type (va/resolve-type viewmodel)
          view-graph (graph/compile (view-type views))
          composition (view-graph view)
          container-component (:container composition)
          rendered-viewed (container-component composition)]
      (rum/mount rendered-viewed (. js/document (querySelector element))))))

(defn new [appstate components url-helpers handlers]
  (map->View {:appstate    appstate
              :components  components
              :handlers    handlers
              :url-helpers url-helpers}))
