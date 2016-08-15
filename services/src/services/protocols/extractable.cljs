(ns services.protocols.extractable
  (:require [cljs.nodejs :as node]
            [models.event.index :refer [extract-data]]
            [services.helpers :as helpers]))

(def marshaler (node/require "dynamodb-marshaler"))
(def unmarshal-item (.-unmarshalItem marshaler))

(defn extract-payload [records]
  (map #(-> %1 :kinesis :data helpers/buffer->clj) records))

(defmethod extract-data :kinesis [records]
  (->> records
       extract-payload
       (map (fn [course] (update-in course [:type] #(keyword %))))))

(defmethod extract-data :dynamodb [records]
  (->> records
       (filter #(= "INSERT" (:eventName %1)))
       (map #(-> % :dynamodb :NewImage clj->js unmarshal-item helpers/js->cljs))))
