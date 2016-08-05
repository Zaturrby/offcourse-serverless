(ns specs.event
  (:require [cljs.spec :as spec]
            [specs.payload :as payload]))

(spec/def ::kinesis map?)
(spec/def ::dynamo map?)

(spec/def ::kinesis-record (spec/keys :req-un [::kinesis]))
(spec/def ::dynamo-record (spec/keys :req-un [::dynamodb]))

(spec/def ::Records (spec/or :kinesis (spec/* ::kinesis-record)
                             :dynamodb (spec/* ::dynamo-record)))

(spec/def ::type keyword?)

(spec/def ::api-event (spec/keys :req-un [::type ::payload/payload]))

(spec/def ::kinesis-event (spec/and (spec/keys :req-un [::Records])
                                    #(= (first (:Records %)) :kinesis)))

(spec/def ::dynamo-event (spec/and (spec/keys :req-un [::Records])
                                    #(= (first (:Records %)) :dynamodb)))

(spec/def ::event (spec/or :kinesis ::kinesis-event
                           :dynamodb ::dynamo-event
                           :api ::api-event))
