(ns services.protocols.extractable
  (:require [cljs.nodejs :as node]
            [protocols.extractable :refer [Extractable]]
            [models.event.index :refer [Event]]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [services.helpers :as helpers]))

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

(extend-type Event
  Extractable
  (-extract [data] (extract-data (:Records data))))
