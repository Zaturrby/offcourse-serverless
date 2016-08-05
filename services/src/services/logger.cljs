(ns services.logger
  (:require [cljs.spec :as spec]
            [specs.core :as specs]))

(defn stringify [obj]
  (.stringify js/JSON (clj->js obj)))

(defn log [msg payload]
  (println msg (stringify payload) "\n"))

(defn pipe [msg payload]
  (log msg payload)
  payload)

(defn log-error [reason action]
  (let [error (clj->js {:type :error
                        :error reason
                        :payload (spec/explain-str ::specs/action action)})]
    (log "Error " error)))
