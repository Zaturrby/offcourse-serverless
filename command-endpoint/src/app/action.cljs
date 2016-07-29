(ns app.action
  (:require [cljs.spec :as spec]
            [app.specs.index :as specs]
            [app.logger :as logger]
            [cljs.spec.test :as stest]
            [clojure.string :as str]))

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

(spec/fdef -convert
           :args ::specs/event
           :ret ::specs/action)

(defmulti -convert (fn [event]
                     (first (spec/conform ::specs/event event))))

(defmethod -convert :api [{:keys [type payload]}]
  (let [action {:payload (update payload :type #(keyword %))
                :type (keyword type)}]
    (if (spec/valid? ::specs/action action)
      (do
        (logger/log "INCOMING: " action)
        action)
      (logger/log-error :invalid-incoming-action action))))

(defmethod -convert :kinesis [event]
  (let [records (:Records event)
        payload (first (extract-payload records))
        event-source (extract-event-source (first records))
        action {:payload payload
                :type event-source}]
    (if (spec/valid? ::specs/action action)
      (do
        (logger/log "INCOMING: " action)
        action)
      (logger/log-error :invalid-incoming-action action))))

(defn convert [event]
  (logger/log "Event: " event)
  (-> event
      (js->clj :keywordize-keys true)
      -convert))

(spec/instrument #'-convert)
