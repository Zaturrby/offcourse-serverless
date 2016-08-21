(ns services.logger
  (:require [cljs.spec :as spec]
            [shared.specs.core :as specs]))

(defn stringify [obj]
  (.stringify js/JSON (clj->js obj)))

#_(defn log [msg payload]
  (println msg (stringify payload) "\n"))

(defn log [& args]
  (.apply js/console.log js/console (to-array args)))

(defn pipe [msg payload]
  (log msg payload)
  payload)

(defn log-error [reason action]
  (let [error (clj->js {:type :error
                        :error reason
                        :payload (spec/explain-str ::specs/action action)})]
    (log "Error " error)))
