(ns shared.models.event.index
  (:require [cljs.spec :as spec]
            [shared.models.event.to-action :refer [to-action]]
            [shared.models.event.to-models :refer [to-models]]
            [shared.models.event.to-query :refer [to-query]]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.protocols.validatable :as va :refer [Validatable]]
            [shared.specs.core :as specs]
            [services.logger :as logger]))

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
    (-resolve-payload [[data-type payload]]
      [data-type (first payload)])
    (-resolve-type [[data-type :as this]]
      (let [payload-type (-> (spec/conform (:spec (meta this)) this) second first)]
        [data-type payload-type]))))

(defn create [[source type payload]]
  (-> [(keyword type) payload]
      (with-meta {:spec ::specs/event
                  :source source
                  :timestamp (.now js/Date)})
      override))

#_(stest/instrument `create)
