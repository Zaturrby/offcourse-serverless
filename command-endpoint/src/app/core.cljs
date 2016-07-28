(ns app.core
  (:require [cljs.nodejs :as node]
            [app.specs.index :as specs]
            [app.message :as message]
            [cljs.spec :as spec]
            [cljs.core.async :refer [<! chan >!]]
            [app.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [event context cb]
  (go
    (let [{:keys [type payload] :as action} (js->clj event :keywordize-keys true)]
      (logger/log "INCOMING: " action)
      (if (spec/valid? ::specs/action action)
        (do
          (<! (message/send {:type type
                             :payload [payload]} :curator))
          (cb nil (clj->js action)))
        (cb nil (clj->js (spec/explain-data ::specs/action action)))))))

(defn -main [] identity)
(set! *main-cli-fn* -main)
