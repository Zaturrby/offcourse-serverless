(ns shared.models.query.index
  (:require [shared.protocols.validatable :refer [Validatable]]
            [specs.core :as specs]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]))

(defrecord Query [])

(spec/fdef create
           :args (spec/cat :query ::specs/query)
           :ret ::specs/query
           :fn #(spec/valid? ::specs/meta (-> %1 :ret :meta)))

(defn create [query]
  (with-meta (map->Query query) {:spec ::specs/query}))

(stest/instrument `create)
