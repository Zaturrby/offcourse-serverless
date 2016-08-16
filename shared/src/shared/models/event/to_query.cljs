(ns shared.models.event.to-query
  (:require [cljs.spec :as spec]
            [specs.core :as specs]
            [services.logger :as logger]))

(spec/fdef to-query
           :args (spec/cat :event ::specs/event)
           :ret (spec/nilable ::specs/query))

(defn to-query [{:keys [payload]}]
  (if (spec/valid? ::specs/query payload)
    (logger/pipe "INCOMING QUERY: " payload)
    (logger/log-error :invalid-incoming-query payload)))
