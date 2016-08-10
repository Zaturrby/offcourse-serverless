(ns app.core
  (:require [protocols.convertible :as cv]
            [cljs.core.async :refer [<!]]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [services.logger :as logger]
            [services.elastic-search.index :as es]
            [cljs.nodejs :as node]
            [services.helpers :as helpers])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [raw-event context cb]
  (if-let [query (-> raw-event cv/to-event cv/to-query)]
    (go
      (let [courses (<! (es/fetch query))]
        (cb nil (.stringify js/JSON (clj->js courses)))))
    (cb "invalid action" nil)))

(defn -main [] identity)
(set! *main-cli-fn* -main)
