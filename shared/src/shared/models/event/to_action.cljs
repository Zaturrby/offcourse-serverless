(ns shared.models.event.to-action
  (:require [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [services.helpers :as helpers]
            [services.logger :as logger]))

(spec/fdef to-action
           :args (spec/cat :event ::specs/event)
           :ret (spec/nilable ::specs/action))

(defn to-action [event]
  (let [action (update-in event [:type]  #(keyword %))]
    (if (spec/valid? ::specs/action action)
      (logger/pipe "INCOMING: " action)
      (logger/log-error :invalid-incoming-action action))))

