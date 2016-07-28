(ns app.message
  (:require [app.logger :as logger]
            [cljs.core.async :refer [>! chan]]
            [cljs.nodejs :as node])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def ^:private AWS (node/require "aws-sdk"))
(def Kinesis (new AWS.Kinesis))

(defn create [{:keys [type payload]} partition-key]
  {:StreamName (str type "-"(.. js/process -env -SERVERLESS_STAGE))
   :Records (->> payload
                 (map (fn [item]
                        {:Data (.stringify js/JSON (clj->js item))
                         :PartitionKey partition-key})))})

(defn send [action partition-key]
  (let [c (chan)
        message (create action partition-key)]
    (logger/log "OUTGOING: " message)
    (.putRecords Kinesis (clj->js message)
                 #(if %1
                    (println "error" %1)
                    (go (>! c %2))))
    c))
