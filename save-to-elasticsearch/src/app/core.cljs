(ns app.core
  (:require [cljs.nodejs :as node]
            [app.specs.index :as specs]
            [app.action :as action]
            [app.es :as es]
            [app.event :as event]
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
        (let [errors (filter :error (<! (es/save payload)))]
          (if (empty? errors)
            (cb nil "Save Succeeded")
            (do
              (logger/log "ERRORS SAVING:" errors)
              (cb "Errors Saving" nil)))))
      (cb "Invalid Event" nil))))

(defn -main [] identity)
(set! *main-cli-fn* -main)
