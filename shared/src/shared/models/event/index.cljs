(ns shared.models.event.index
  (:require [shared.models.event.to-action :refer [to-action]]
            [shared.models.event.to-payload :refer [to-payload]]
            [shared.models.event.to-query :refer [to-query]]
            [shared.models.course.index :as co]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.protocols.extractable :refer [Extractable]]
            [shared.protocols.validatable :as va]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [services.helpers :as helpers]
            [services.logger :as logger]))

(defmulti extract-data (fn [event] (va/resolve-type event)))

(defmethod extract-data :offcourse [{:keys [payload]}]
  payload)

(defrecord Event []
  Convertible
  (-to-action [this] (to-action this))
  (-to-payload [this] (to-payload this))
  (-to-query [this] (to-query this))
  (-to-models [this] (map co/create (cv/to-payload this)))
  Extractable
  (-extract [this] (extract-data this)))

(defmulti create (fn [event] (first (spec/conform ::specs/map-type event))))

(defmethod create :raw [raw-event]
  (-> raw-event
      (js->clj :keywordize-keys true)
      helpers/keywordize-type
      map->Event
      (with-meta {:spec ::specs/event})))


(defmethod create :default [event]
  (-> event
      map->Event
      (with-meta {:spec ::specs/event})))

(extend-protocol Convertible
  object
  (-to-event [raw] (create raw))
  PersistentArrayMap
  (-to-event [raw] (create raw)))
