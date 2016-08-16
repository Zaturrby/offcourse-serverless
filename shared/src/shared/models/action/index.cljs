(ns shared.models.action.index
  (:require [shared.protocols.validatable :refer [Validatable]]
            [shared.specs.core :as specs]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]))

(spec/fdef create
           :args (spec/cat :action ::specs/action)
           :ret ::specs/action
           :fn #(spec/valid? ::specs/meta (-> %1 :ret :meta)))

(defn create [query] (with-meta query {:spec ::specs/action}))

(stest/instrument `create)
