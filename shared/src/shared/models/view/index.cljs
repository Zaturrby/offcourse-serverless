(ns shared.models.view.index
  (:require [plumbing.graph :as graph]
            [rum.core :as rum]
            [shared.protocols.renderable :as rr :refer [Renderable]]
            [shared.protocols.validatable :as va]))

(defrecord View []
  Renderable
  (-render [view views]
    (let [view-type (-> view :appstate :viewmodel va/resolve-type)]
      ((graph/compile (view-type views)) view))))

(defn create [view-data]
  (map->View view-data))
