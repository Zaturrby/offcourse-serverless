(ns app.core
  (:require [cljs.nodejs :as node]
            [services.db :as db]
            [app.event :as event]
            [cljs.core.async :refer [<! chan >!]]
            [services.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [event context cb]
  (if-let [payload (event/to-payload event)]
    (go
      (let [errors (filter :error (<! (db/save payload)))]
        (println errors)
        (if (empty? errors)
          (cb nil "Save Succeeded")
          (do
            (logger/log "ERRORS SAVING:" errors)
            (cb "Errors Saving" nil)))))
    (cb "Invalid Event" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
