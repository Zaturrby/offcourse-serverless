(ns shared.models.command.index
  (:require [shared.protocols.validatable :as va :refer [Validatable]]
            [shared.specs.core :as specs]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [services.logger :as logger]))

(spec/fdef create
           :args (spec/cat :action ::specs/command)
           :ret ::specs/command
           :fn #(spec/valid? ::specs/meta (-> %1 :ret :meta)))

(defn override [command]
  (specify command
    Validatable
    (-resolve-type [[data-type :as this]]
      (let [payload-type (-> (spec/conform (:spec (meta this)) this) second first)]
        [data-type payload-type]))))

(defn create [command]
  (-> command
      (with-meta {:spec ::specs/command})
      override))

(stest/instrument `create)
