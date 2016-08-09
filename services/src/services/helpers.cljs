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

(defn keywordize-type [event]
  (if (:type event)
    (update-in event [:type] #(keyword %))
    event))
