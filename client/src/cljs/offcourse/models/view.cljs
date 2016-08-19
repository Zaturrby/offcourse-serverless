(ns offcourse.models.view
  (:require [offcourse.protocols.composable :as ca :refer [Composable]]
            [offcourse.protocols.mountable :as ma :refer [Mountable]]
            [offcourse.protocols.renderable :as rr :refer [Renderable]]
            [shared.protocols.validatable :as va :refer [Validatable]]
            [plumbing.graph :as graph]
            [rum.core :as rum]
            [offcourse.protocols.queryable :as qa]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [services.logger :as logger]))

(defrecord View []
  Composable
  (-compose [view views]
    (let [viewmodel (-> view :appstate :viewmodel)
          view-type (va/resolve-type viewmodel)]
      (assoc view :composition
             ((graph/compile (view-type views)) view))))
  Renderable
  (-render [{:keys [composition] :as view}]
    (assoc view :rendered
           ((:container composition) composition)))
  Mountable
  (-mount [{:keys [rendered]} element]
    (rum/mount rendered (. js/document (querySelector element)))))

(defn new [appstate components url-helpers handlers]
  (map->View {:appstate    appstate
              :components  components
              :handlers    handlers
              :url-helpers url-helpers}))
