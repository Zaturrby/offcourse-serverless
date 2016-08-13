(ns app.core
  (:require [protocols.convertible.index :as cv]
            [protocols.extractable :refer [Extractable]]
            [cljs.core.async :refer [<!]]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [models.event.index :refer [Event]]
            [services.logger :as logger]
            [services.elastic-search.index :as es]
            [cljs.nodejs :as node]
            [services.helpers :as helpers])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn create-response [data]
  {:type :found-data
   :payload data})

(defn ^:export handler [raw-event context cb]
  (logger/log "Event" raw-event)
  (if-let [query (-> raw-event cv/to-event cv/to-query)]
    (go
      (let [data (<! (es/fetch query))
            action (create-response data)]
        (if (spec/valid? ::specs/action action)
          (cb nil (.stringify js/JSON (clj->js action)))
          (cb (spec/explain ::specs/action action) nil))))
    (cb "invalid query" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
