(ns shared.models.action.index
  (:require [shared.protocols.validatable :as va :refer [Validatable]]
            [shared.specs.core :as specs]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [services.logger :as logger]))

(spec/fdef create
           :args (spec/cat :action ::specs/action)
           :ret ::specs/action
           :fn #(spec/valid? ::specs/meta (-> %1 :ret :meta)))

(defn- override [action]
  (specify action
    Validatable
    (-resolve-type [[data-type payload :as this]]
      (if-let [payload-type (-> (spec/conform (:spec (meta this)) this) second first)]
        [data-type payload-type]
        [data-type payload]))))

(defn create
  "creates a new action"
  [action]
  (-> action
      (with-meta {:spec ::specs/action})
      override))

(stest/instrument `create)
