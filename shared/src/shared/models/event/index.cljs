(ns shared.models.event.index
  (:require [shared.models.event.to-action :refer [to-action]]
            [shared.models.event.to-query :refer [to-query]]
            [shared.models.event.to-models :refer [to-models]]

            [shared.models.course.index :as co]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.protocols.validatable :as va :refer [Validatable]]
            [shared.protocols.extractable :refer [Extractable]]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [services.helpers :as helpers]
            [services.logger :as logger]
            [cljs.spec.test :as stest]))

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

(spec/fdef create
           :args (spec/cat :event-type ::specs/event)
           :ret ::specs/event
           :fn #(spec/valid? ::specs/meta (-> %1 :ret :meta)))

(defn override [event]
  (specify event
    Convertible
    (-to-action [this] (to-action this))
    (-to-query [this] (to-query this))
    (-to-models [this] (to-models this))
    Extractable
    (-extract [this] (extract-data this))
    Validatable
    (-resolve-type [[data-type :as this]]
      (let [payload-type (-> (spec/conform (:spec (meta this)) this) second first)]
        [data-type payload-type]))))

(defn create [[source type payload]]
  (let [event (-> [(keyword type) payload]
                  (with-meta {:spec ::specs/event
                              :source source
                              :timestamp (.now js/Date)})
                  override)]
    (logger/pipe "Event" event)))

#_(stest/instrument `create)

(extend-protocol Convertible
  object
  (-to-event [raw]
    (logger/log "RAW" type raw)
    (-> raw
        (js->clj :keywordize-keys true)
        helpers/keywordize-type
        create))
  PersistentArrayMap
  (-to-event [raw] (create raw)))
