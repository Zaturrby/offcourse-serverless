(ns services.s3
  (:require [services.logger :as logger]
            [cljs.core.async :as async :refer [>! chan]]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [cljs.nodejs :as node]
            [clojure.walk :as walk]
            [clojure.string :as str]))

(def AWS (node/require "aws-sdk"))
(def dynamo (AWS.DynamoDB.DocumentClient.))
(def s3 (AWS.S3.))
(def bucket-name (.. js/process -env -BUCKET_NAME))

(defn -save [query]
  (let [c (chan)]
    (.putObject s3 (clj->js query)
          #(let [response (if %1
                            {:error %1}
                            {:success (:id (:Item query))})]
             (when (= :error response)
               (logger/log "Error Saving Item: " query))
             (async/put! c response)
             (async/close! c)))
    c))

(defn create-query [folder-name item]
  (let [{:keys [curator course-id]} item
        file-name (second (str/split course-id "::"))
        file-path (str curator "/" folder-name "/" file-name ".json")]
        {:Bucket bucket-name
         :Key file-path
         :Body (.stringify js/JSON (clj->js item))}))

(defn save [payload]
  (let [folder-name (-> (spec/conform ::specs/valid-payload payload) first name)
        queries (map #(create-query folder-name %1) payload)
        query-chans (async/merge (map -save queries))]
    (async/into [] query-chans)))

