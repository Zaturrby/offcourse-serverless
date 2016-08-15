(ns models.event.index
  (:require [models.event.to-action :refer [to-action]]
            [models.event.to-payload :refer [to-payload]]
            [models.event.to-query :refer [to-query]]
            [models.course.index :as co]
            [protocols.convertible.index :as cv :refer [Convertible]]
            [protocols.extractable :refer [Extractable]]
            [protocols.validatable :as va]
            [cljs.spec :as spec]
            [specs.core :as specs]
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

(defn create [raw-event]
  (-> raw-event
      (js->clj :keywordize-keys true)
      helpers/keywordize-type
      map->Event
      (with-meta {:spec ::specs/event})))

(extend-protocol Convertible
  object
  (-to-event [raw] (create raw))
  PersistentArrayMap
  (-to-event [raw] (create raw)))
