(ns app.core
  (:require [cljs.nodejs :as node]
            [protocols.convertible.index :as cv]
            [cljs.core.async :refer [<! chan >!]]
            [services.elastic-search.index :as es]
            [services.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(node/enable-util-print!)
(def AWS (node/require "aws-sdk"))

(defn ^:export handler [raw-event context cb]
  (logger/log "Event" raw-event)
  (if-let [payload (-> raw-event cv/to-event cv/to-payload)]
    (go
      (let [res (<! (es/save payload))
            errors (filter :error res)]
        (if (empty? errors)
          (cb nil "Save Succeeded")
          (do
            (logger/log "ERRORS SAVING:" errors)
            (cb "Errors Saving" nil)))))
    (cb "Invalid Event" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
