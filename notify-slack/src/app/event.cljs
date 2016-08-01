(ns app.event
  (:require [cljs.nodejs :as node]
            [app.specs.index :as specs]
            [app.action :as action]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [cljs.core.async :refer [<! chan >!]]
            [app.logger :as logger]))

(def AWS (node/require "aws-sdk"))
(def marshaler (node/require "dynamodb-marshaler"))
(def unmarshal-item (.-unmarshalItem marshaler))

(defn json->clj [data]
  (-> (.parse js/JSON data "ascii")
      (js->clj :keywordize-keys true)))

(defn js->cljs [item]
  (js->clj item :keywordize-keys true))

(defn buffer->clj [data]
  (-> data
      (js/Buffer "base64")
      (.toString "ascii")
      json->clj))

(defn extract-data [records]
  (->> records
       (filter #(= "INSERT" (:eventName %1)))
       (map #(-> % :dynamodb :NewImage clj->js unmarshal-item js->cljs))))

(defn dynamo-to-payload [event]
  (let [records (:Records event)
        data    (extract-data records)
        [type]  (spec/conform ::specs/data-types data)
        payload {:type type
                 type  data}]
    (if (spec/valid? ::specs/payload payload)
      (do
        (logger/log "INCOMING PAYLOAD: " payload)
        payload)
      (logger/log-error :invalid-incoming-action payload))))
