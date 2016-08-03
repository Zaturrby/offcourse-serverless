(ns app.core
  (:require [cljs.nodejs :as node]
            [specs.core :as specs]
            [app.stream :as stream]
            [app.action :as action]
            [cljs.spec :as spec]
            [cljs.core.async :refer [<! chan >!]]
            [app.logger :as logger])
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
