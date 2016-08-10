(ns services.elastic-search.save
  (:require [cljs.core.async :as async]
            [cljs.spec :as spec]
            [services.elastic-search.save-request :as request]
            [specs.core :as specs]))


(defn save [payload]
  (let [type (-> (spec/conform ::specs/valid-payload payload) first name)
        query-chans (async/merge (map #(request/send type %) payload))]
    (async/into [] query-chans)))
