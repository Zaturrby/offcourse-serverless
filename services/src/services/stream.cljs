(ns services.stream
  (:require [services.logger :as logger]
            [cljs.core.async :as async :refer [>! chan]]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [cljs.nodejs :as node])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def ^:private AWS (node/require "aws-sdk"))
(def Kinesis (new AWS.Kinesis))
(def stage (.. js/process -env -SERVERLESS_STAGE))
(def suffix (or (.. js/process -env -STREAM_SUFFIX) "data"))

(defmulti create (fn [data _]
                   (first (spec/conform ::specs/single-or-multiple? data))))

(defmethod create :multiple [items partition-key]
  (let [[type] (spec/conform ::specs/payload items)]
    {:StreamName (str (name type) "-" suffix "-" stage)
     :Records (->> items
                   (map (fn [item]
                          {:Data (.stringify js/JSON (clj->js item))
                           :PartitionKey partition-key})))}))

(defmethod create :single [item partition-key]
  (create [item] partition-key))

(defn -send [message]
  (let [c (chan)]
    (.putRecords Kinesis (clj->js message) #(if %1
                                              (println "error" %1)
                                              (async/put! c %2)))
    c))

(defn send [data partition-key]
  (let [message (create data partition-key)]
    (logger/log "OUTGOING: " message)
    (-send message)))

