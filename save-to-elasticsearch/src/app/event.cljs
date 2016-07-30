(ns app.event
  (:require [cljs.nodejs :as node]
            [app.specs.index :as specs]
            [app.action :as action]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [cljs.core.async :refer [<! chan >!]]
            [app.logger :as logger]))

(defn json->clj [data]
  (-> (.parse js/JSON data "ascii")
      (js->clj :keywordize-keys true)))

(defn buffer->clj [data]
  (-> data
      (js/Buffer "base64")
      (.toString "ascii")
      json->clj))

(defn extract-data [records]
  (->> records
       (map #(-> %1 :kinesis :data buffer->clj))
       (map (fn [course] (update-in course [:type] #(keyword %))))))

(defn -convert [event]
  (let [records     (:Records event)
        data        (extract-data records)
        record-type (:type (first data))
        records     (map record-type data)
        type        (keyword (str (name record-type) "s"))
        payload     {:type type
                     type  records}]
    (if (spec/valid? ::specs/payload payload)
      (do
        (logger/log "INCOMING PAYLOAD: " payload)
        payload)
      (logger/log-error :invalid-incoming-action payload))))

(defn to-payload [event]
  (logger/log "Event: " event)
  (-> event
      (js->clj :keywordize-keys true)
      -convert))
