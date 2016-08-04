(ns services.helpers
  (:require [clojure.string :as str]
            [models.event.index :as event]
            [services.logger :as logger]))

(defn json->clj [data]
  (-> (.parse js/JSON data "ascii")
      (js->clj :keywordize-keys true)))

(defn buffer->clj [data]
  (-> data
      (js/Buffer "base64")
      (.toString "ascii")
      json->clj))

(defn extract-payload [records]
  (map #(-> %1 :kinesis :data buffer->clj) records))

(defn extract-event-source [record]
  (-> record
      :eventSourceARN
      (str/split "/")
      last))

(defn extract-data [records]
  (->> records
       extract-payload
       (map (fn [course] (update-in course [:type] #(keyword %))))))

(defn keywordize-type [event]
  (if (:type event)
    (update-in event [:type] #(keyword %))
    event))

(defn to-event [raw-event]
  (logger/log "Event: " raw-event)
  (-> raw-event
      (js->clj :keywordize-keys true)
      keywordize-type
      event/map->Event))
