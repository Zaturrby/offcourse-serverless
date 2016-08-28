(ns shared.specs.event
  (:require [cljs.spec :as spec]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.credentials :as credentials]
            [shared.specs.payload :as payload]
            [shared.specs.query :as query]
            [shared.specs.helpers :as helpers]
            [shared.specs.action :as action]))

(spec/def ::kinesis map?)
(spec/def ::dynamo map?)

(spec/def ::kinesis-record (spec/keys :req-un [::kinesis]))
(spec/def ::dynamo-record (spec/keys :req-un [::dynamodb]))

(spec/def ::Records (spec/or :kinesis (spec/* ::kinesis-record)
                             :dynamodb (spec/* ::dynamo-record)))

(spec/def ::type keyword?)

(spec/def ::api-event (spec/keys :req-un [::type]))

(spec/def ::kinesis-event (spec/and (spec/keys :req-un [::Records])
                                    #(= (first (:Records %)) :kinesis)))

(spec/def ::dynamo-event (spec/and (spec/keys :req-un [::Records])
                                    #(= (first (:Records %)) :dynamodb)))

#_(spec/def ::event (spec/or :kinesis ::kinesis-event
                           :dynamodb ::dynamo-event
                           :offcourse ::api-event))

(spec/def ::event-payload (spec/or :viewmodel   ::viewmodel/viewmodel
                                   :credentials ::credentials/credentials
                                   :action      ::action/action
                                   :data        ::payload/payload
                                   :query       ::query/query))


(spec/def ::event (helpers/tuple-spec [:found-data :found :not-found :granted :revoked :requested
                                       :requested-data :rendered :refreshed] ::event-payload))
