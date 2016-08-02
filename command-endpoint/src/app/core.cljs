(ns app.core
  (:require [cljs.nodejs :as node]
            [app.specs.index :as specs]
            [specs.core :as s]
            [app.message :as message]
            [app.action :as action]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [cljs.core.async :refer [<! chan >!]]
            [app.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def AWS (node/require "aws-sdk"))

(node/enable-util-print!)

(defn ^:export handler [event context cb]
  (println (s/foo "HI"))
  (go
    (let [action (action/convert event)]
      (if (spec/valid? ::specs/action action)
        (do
          (<! (message/send (:payload action) :curator))
          (cb nil (clj->js action)))
        (cb nil (clj->js (spec/explain-data ::specs/action action)))))))

(defn -main [] identity)
(set! *main-cli-fn* -main)
