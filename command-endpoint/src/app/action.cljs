(ns app.action
  (:require [cljs.spec :as spec]
            [specs.core :as specs]
            [services.logger :as logger]
            [services.helpers :as helpers]
            [cljs.spec.test :as stest]
            [clojure.string :as str]))

(spec/fdef -convert
           :args ::specs/event
           :ret ::specs/action)

(defn -convert [{:keys [type payload]}]
  (let [action {:payload (update payload :type #(keyword %))
                :type (keyword type)}]
    (if (spec/valid? ::specs/action action)
      (do
        (logger/log "INCOMING: " action)
        action)
      (logger/log-error :invalid-incoming-action action))))

(defn convert [event]
  (logger/log "Event: " event)
  (-> event
      (js->clj :keywordize-keys true)
      -convert))

#_(spec/instrument #'-convert)
