(ns app.core
  (:require [protocols.convertible :as cv]
            [services.stream :as stream]
            [cljs.core.async :refer [<!]]
            [services.logger :as logger]
            [cljs.nodejs :as node]
            [services.helpers :as helpers])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [raw-event context cb]
  (if-let [action (-> raw-event cv/to-event cv/to-action)]
    (go
      (<! (stream/send (:payload action) :curator))
      (cb nil "payload sent"))
    (cb "invalid action" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
