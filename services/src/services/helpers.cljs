(ns services.helpers
  (:require [clojure.string :as str]
            [cljs.spec :as spec]
            [services.logger :as logger]))

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

(defn resolve-data-type [spec data] (first (spec/conform spec data)))
