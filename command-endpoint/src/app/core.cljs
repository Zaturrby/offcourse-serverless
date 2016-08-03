(ns app.core
  (:require [app.action :as action]
            [app.stream :as stream]
            [cljs.core.async :refer [<!]]
            [cljs.nodejs :as node])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [event context cb]
  (go
    (if-let [action (action/convert event)]
      (do
        (<! (stream/send (:payload action) :curator))
        (cb nil (clj->js action)))
      (cb "invalid action" nil))))

(defn -main [] identity)
(set! *main-cli-fn* -main)
