(ns app.core
  (:require [cljs.nodejs :as node]
            [services.db :as db]
            [protocols.convertible.index :as cv]
            [cljs.core.async :refer [<! chan >!]]
            [services.helpers :as helpers]
            [services.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [raw-event context cb]
  (if-let [payload (-> raw-event cv/to-event cv/to-payload)]
    (go
      (let [errors (filter :error (<! (db/save payload)))]
        (if (empty? errors)
          (cb nil "Save Succeeded")
          (do
            (logger/log "ERRORS SAVING:" errors)
            (cb "Errors Saving" nil)))))
    (cb "Invalid Payload" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
