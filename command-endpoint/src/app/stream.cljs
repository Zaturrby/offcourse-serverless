(ns app.stream
  (:require [services.logger :as logger]
            [cljs.core.async :refer [>! chan]]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [cljs.nodejs :as node])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def ^:private AWS (node/require "aws-sdk"))
(def Kinesis (new AWS.Kinesis))

#_(defn create [{:keys [type payload]} partition-key]
  {:StreamName (str type "-" (.. js/process -env -SERVERLESS_STAGE))
   :Records (->> payload
                 (map (fn [item]
                        {:Data (.stringify js/JSON (clj->js item))
                         :PartitionKey partition-key})))})

(defn create [item partition-key]
  (let [[type :as i] (spec/conform ::specs/payload item)]
    (println i)
    {:StreamName (str (name type) "-" (.. js/process -env -SERVERLESS_STAGE))
     :Data (.stringify js/JSON (clj->js item))
     :PartitionKey partition-key}))

(defn send [item partition-key]
  (let [c (chan)
        message (create item partition-key)]
    (logger/log "OUTGOING: " message)
    (.putRecord Kinesis (clj->js message)
                 #(if %1
                    (println "error" %1)
                    (go (>! c %2))))
    c))
