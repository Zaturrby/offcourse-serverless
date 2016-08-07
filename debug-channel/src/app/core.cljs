(ns app.core
  (:require [cljs.nodejs :as node]
            [protocols.convertible :as cv]
            [services.notifier :as notify]
            [cljs.core.async :refer [<! chan >!]]
            [services.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(node/enable-util-print!)
(def AWS (node/require "aws-sdk"))

(defn ^:export handler [raw-event context cb]
  (if-let [event (-> raw-event cv/to-event)]
    (cb nil "Logged Event")
    (cb "Invalid Event" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
