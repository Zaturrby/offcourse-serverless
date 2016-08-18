(ns shared.models.event.index
  (:require [shared.models.event.to-action :refer [to-action]]
            [shared.models.event.to-query :refer [to-query]]
            [shared.models.event.to-models :refer [to-models]]
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
  (-to-query [this] (to-query this))
  (-to-models [this] (to-models this))
  Extractable
  (-extract [this] (extract-data this)))

#_(defmulti create (fn [event] (first (spec/conform ::specs/map-type event))))

#_(defmethod create :raw [raw-event]
  (-> raw-event
      (js->clj :keywordize-keys true)
      helpers/keywordize-type
      map->Event
      (with-meta {:spec ::specs/event})))

(defn override [command]
  (specify command
    Convertible
    (-to-action [this] (to-action this))
    (-to-query [this] (to-query this))
    (-to-models [this] (to-models this))
    Extractable
    (-extract [this] (extract-data this))))

(defn create [[type payload]]
  (-> [(keyword type) payload]
      (with-meta {:spec ::specs/event})
      override))

(extend-protocol Convertible
  object
  (-to-event [raw]
    (logger/log "RAW" raw)
    (-> raw
        (js->clj :keywordize-keys true)
        helpers/keywordize-type
        create))
  PersistentArrayMap
  (-to-event [raw] (create raw)))
