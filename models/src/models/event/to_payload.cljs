(ns models.event.to-payload
  (:require [cljs.spec :as spec]
            [specs.core :as specs]
            [services.helpers :as helpers]
            [services.logger :as logger]))

(defmulti to-payload (fn [event] (first (spec/conform ::specs/event event))))

(defmethod to-payload :kinesis [event]
  (let [records     (:Records event)
        data        (helpers/extract-data records)
        record-type (:type (first data))
        records     (map record-type data)
        type        (keyword (str (name record-type) "s"))
        payload     {:type type
                     type records}]
    (if (spec/valid? ::specs/payload payload)
      (logger/pipe "INCOMING PAYLOAD: " payload)
      (logger/log-error :invalid-incoming-payload payload))))

(defmethod to-payload :dynamodb [event]
  (let [records (:Records event)
        data    (helpers/extract-data records)
        [type]  (spec/conform ::specs/data-types data)
        payload {:type type
                 type  data}]
    (if (spec/valid? ::specs/payload payload)
      (logger/pipe "INCOMING PAYLOAD: " payload)
      (logger/log-error :invalid-incoming-action payload))))
