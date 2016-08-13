(ns app.core
  (:require [cljs.core.async :refer [<!]]
            [cljs.nodejs :as node]
            [protocols.convertible.index :as cv]
            [services.logger :as logger]
            [services.stream :as stream])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [raw-event context cb]
  (logger/log "Event" raw-event)
  (if-let [action (-> raw-event cv/to-event cv/to-action)]
    (go
      (<! (stream/send (:payload action) :curator))
      (cb nil "payload sent"))
    (cb "invalid action" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
