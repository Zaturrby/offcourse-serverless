(ns app.event
  (:require [cljs.nodejs :as node]
            [specs.core :as specs]
            [cljs.spec :as spec]
            [cljs.core.async :refer [<! chan >!]]
            [services.helpers :as helpers]
            [services.logger :as logger]))

(defn -convert [event]
  (let [records     (:Records event)
        data        (helpers/extract-data records)
        record-type (:type (first data))
        records     (map record-type data)
        type        (keyword (str (name record-type) "s"))
        payload     {:type type
                     :payload-data  records}]

    #_(println (spec/conform ::specs/data-types records))

    (if (spec/valid? ::specs/payload payload)
      (do
        (logger/log "INCOMING PAYLOAD: " payload)
        payload)
      (logger/log-error :invalid-incoming-action payload))))

(defn to-payload [event]
  (logger/log "Event: " event)
  (-> event
      (js->clj :keywordize-keys true)
      -convert))
