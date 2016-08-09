(ns models.event.to-payload
  (:require [cljs.spec :as spec]
            [specs.core :as specs]
            [cljs.nodejs :as node]
            [specs.payload :as pl-specs]
            [services.helpers :as helpers]
            [services.logger :as logger]))

(def marshaler (node/require "dynamodb-marshaler"))
(def unmarshal-item (.-unmarshalItem marshaler))

(defn extract-payload [records]
  (map #(-> %1 :kinesis :data helpers/buffer->clj) records))

(defmulti extract-data (fn [records] (first (spec/conform ::specs/Records records))))

(defmethod extract-data :kinesis [records]
  (->> records
       extract-payload
       (map (fn [course] (update-in course [:type] #(keyword %))))))

(defmethod extract-data :dynamodb [records]
  (->> records
       (filter #(= "INSERT" (:eventName %1)))
       (map #(-> % :dynamodb :NewImage clj->js unmarshal-item helpers/js->cljs))))

(defn to-payload [event]
  (let [records     (:Records event)
        payload     (extract-data records)]
    (if (spec/valid? ::pl-specs/valid-payload payload)
      (logger/pipe "INCOMING PAYLOAD: " payload)
      (logger/log-error :invalid-incoming-payload payload))))
