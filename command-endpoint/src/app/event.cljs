(ns app.event
  (:require [cljs.spec :as spec]
            [specs.core :as specs]
            [services.logger :as logger]
            [services.helpers :as helpers]
            [cljs.spec.test :as stest]
            [clojure.string :as str]))

(defrecord Event [])

(defn from-js [raw-event]
  (logger/log "Event: " raw-event)
  (-> raw-event
      (js->clj :keywordize-keys true)
      helpers/keywordize-type
      map->Event))

(spec/fdef to-action
           :args (spec/cat :event ::specs/event)
           :ret (spec/nilable ::specs/action))

(defn to-action [{:keys [type payload]}]
  (let [action {:payload (update payload :type #(keyword %))
                :type (keyword type)}]
    (if (spec/valid? ::specs/action action)
      (logger/pipe "INCOMING: " action)
      (logger/log-error :invalid-incoming-action action))))

(spec/instrument #'to-action)
