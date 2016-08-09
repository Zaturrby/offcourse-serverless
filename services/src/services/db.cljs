(ns services.db
  (:require [services.logger :as logger]
            [cljs.core.async :as async :refer [>! chan]]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [cljs.nodejs :as node]
            [clojure.walk :as walk]))

(def AWS (node/require "aws-sdk"))
(def dynamo (AWS.DynamoDB.DocumentClient.))

(defn replaceEmptyStrings [obj]
  (walk/postwalk-replace {"" nil} obj))

(defn marshal [item]
  (-> item
      replaceEmptyStrings
      clj->js))

(defn create-query [table-name item]
  {:TableName table-name
   :Item (marshal item)})

(defn -save [query]
  (let [c (chan)]
    (.put dynamo (clj->js query)
          #(let [response (if %1
                            {:error %1}
                            {:success (:id (:Item query))})]
             (when (= :error response)
               (logger/log "Error Saving Item: " query))
             (async/put! c response)
             (async/close! c)))
    c))

(defn save [payload]
  (let [type (-> (spec/conform ::specs/valid-payload payload) first name)
        table-name (str type "-" (.. js/process -env -SERVERLESS_STAGE))
        queries (map #(create-query table-name %1) payload)
        query-chans (async/merge (map -save queries))]
    (async/into [] query-chans)))
