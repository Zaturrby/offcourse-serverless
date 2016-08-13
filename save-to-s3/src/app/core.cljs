(ns app.core
  (:require [cljs.nodejs :as node]
            [protocols.convertible.index :as cv]
            [services.s3 :as s3]
            [cljs.core.async :as async :refer [<! chan >!]]
            [services.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(node/enable-util-print!)
(def AWS (node/require "aws-sdk"))

(defn ^:export handler [raw-event context cb]
  (logger/log "Event" raw-event)
  (if-let [payload (-> raw-event cv/to-event cv/to-payload)]
    (go
      (let [errors (filter :error (<! (s3/save payload)))]
        (if (empty? errors)
          (cb nil "Saved Document in S3")
          (do
            (logger/log "ERRORS SAVING:" errors)
            (cb "Errors Saving" nil)))))
    (cb "Invalid Event" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
