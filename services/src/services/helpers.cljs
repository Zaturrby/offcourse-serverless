(ns services.helpers
  (:require [clojure.string :as str]
            [cljs.nodejs :as node]
            [specs.core :as specs]
            [cljs.spec :as spec]
            [services.logger :as logger]))

(def AWS (node/require "aws-sdk"))
(def marshaler (node/require "dynamodb-marshaler"))
(def unmarshal-item (.-unmarshalItem marshaler))

(defn json->clj [data]
  (-> (.parse js/JSON data "ascii")
      (js->clj :keywordize-keys true)))

(defn buffer->clj [data]
  (-> data
      (js/Buffer "base64")
      (.toString "ascii")
      json->clj))

(defn js->cljs [item]
  (js->clj item :keywordize-keys true))

(defn extract-payload [records]
  (map #(-> %1 :kinesis :data buffer->clj) records))

(defn extract-event-source [record]
  (-> record
      :eventSourceARN
      (str/split "/")
      last))

(defmulti extract-data (fn [records] (first (spec/conform ::specs/Records records))))

(defmethod extract-data :kinesis [records]
  (->> records
       extract-payload
       (map (fn [course] (update-in course [:type] #(keyword %))))))

(defmethod extract-data :dynamodb [records]
  (->> records
       (filter #(= "INSERT" (:eventName %1)))
       (map #(-> % :dynamodb :NewImage clj->js unmarshal-item js->cljs))))

(defn keywordize-type [event]
  (if (:type event)
    (update-in event [:type] #(keyword %))
    event))
