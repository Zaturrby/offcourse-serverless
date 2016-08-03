(ns app.core
  (:require [cljs.nodejs :as node]
            [app.event :as event]
            [app.notify :as notify]
            [cljs.core.async :refer [<! chan >!]]
            [app.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [event context cb]
  (logger/log "Event: " event)
  (if-let [payload (event/dynamo-to-payload (js->clj event :keywordize-keys true))]
    (go
      (let [res (<! (notify/notify :slack payload))
            errors (filter :error res)]
        (if (empty? errors)
          (cb nil "Send Slack Notification")
          (do
            (logger/log "ERRORS SAVING:" errors)
            (cb "Errors Saving" nil)))))
    (cb "Invalid Event" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
