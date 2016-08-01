(ns app.core
  (:require [cljs.nodejs :as node]
            [app.specs.index :as specs]
            [app.action :as action]
            [app.event :as event]
            [app.notify :as notify]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [cljs.core.async :refer [<! chan >!]]
            [app.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [event context cb]
  (logger/log "Event: " event)
  (let [event (js->clj event :keywordize-keys true)
        payload (event/dynamo-to-payload event)]
    (if payload
      (go
        (let [res (<! (notify/notify :slack payload))
              errors (filter :error res)]
          (if (empty? errors)
            (cb nil "Send Slack Notification")
            (do
              (logger/log "ERRORS SAVING:" errors)
              (cb "Errors Saving" nil)))))
      (cb "Invalid Event" nil))))

(defn -main [] identity)
(set! *main-cli-fn* -main)
