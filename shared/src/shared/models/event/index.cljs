(ns shared.models.event.index
  (:require [shared.models.event.to-action :refer [to-action]]
            [shared.models.event.to-query :refer [to-query]]
            [shared.models.event.to-models :refer [to-models]]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.protocols.validatable :as va :refer [Validatable]]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [services.helpers :as helpers]
            [services.logger :as logger]
            [cljs.spec.test :as stest]))

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
