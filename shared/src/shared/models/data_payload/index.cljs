(ns shared.models.data-payload.index
  (:require [shared.protocols.validatable :refer [Validatable]]
            [shared.specs.core :as specs]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]))

(spec/fdef create
           :args (spec/cat :query ::specs/data-payload)
           :ret ::specs/query
           :fn #(spec/valid? ::specs/meta (-> %1 :ret :meta)))

(defn create [query] (with-meta query {:spec ::specs/data-payload}))

(stest/instrument `create)
