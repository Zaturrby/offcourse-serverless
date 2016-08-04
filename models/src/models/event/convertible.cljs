(ns models.event.convertible
  (:require [cljs.spec :as spec]
            [specs.core :as specs]
            [services.logger :as logger]))

(spec/fdef to-action
           :args (spec/cat :event ::specs/event)
           :ret (spec/nilable ::specs/action))

(defn to-action [{:keys [type payload]}]
  (let [action {:payload (update payload :type #(keyword %))
                :type (keyword type)}]
    (if (spec/valid? ::specs/action action)
      (logger/pipe "INCOMING: " action)
      (logger/log-error :invalid-incoming-action action))))
