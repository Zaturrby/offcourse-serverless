(ns app.core
  (:require [protocols.convertible :as cv]
            [app.stream :as stream]
            [cljs.core.async :refer [<!]]
            [services.logger :as logger]
            [cljs.nodejs :as node]
            [services.helpers :as helpers])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [raw-event context cb]
  (let [event (helpers/to-event raw-event)]
    (go
      (if-let [action (cv/to-action event)]
        (do
          (<! (stream/send (:payload action) :curator))
          (cb nil "payload sent"))
        (do
          (cb "invalid action" nil))))))

(defn -main [] identity)
(set! *main-cli-fn* -main)
