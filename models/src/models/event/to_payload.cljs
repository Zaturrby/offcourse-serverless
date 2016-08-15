(ns models.event.to-payload
  (:require [cljs.spec :as spec]
            [specs.core :as specs]
            [services.helpers :as helpers]
            [protocols.extractable :as ex]
            [services.logger :as logger]))

(spec/fdef to-payload
           :args (spec/cat :event ::specs/event)
           :ret (spec/nilable ::specs/payload))

(defn to-payload [{:keys [payload] :as event}]
  (let [payload     (ex/extract event)]
    (if (spec/valid? ::specs/valid-payload payload)
      (logger/pipe "INCOMING PAYLOAD: " payload)
      (logger/log-error :invalid-incoming-payload payload))))
