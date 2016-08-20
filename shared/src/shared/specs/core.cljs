(ns shared.specs.core
  (:require [cljs.spec :as spec]
            [shared.specs.event :as event]
            [shared.specs.payload :as payload]
            [shared.specs.query :as query]
            [shared.specs.data-payload :as data-payload]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.appstate :as appstate]
            [shared.specs.course :as course]
            [shared.specs.checkpoint :as checkpoint]))

(spec/def ::type #{:requested-save
                   :requested-data
                   :found-data})

(spec/def ::valid-payload ::payload/valid-payload)
(spec/def ::payload ::payload/payload)
(spec/def ::action (spec/keys :req-un [::payload ::type]))
(spec/def ::spec spec/spec?)

(spec/def ::query ::query/query)
(spec/def ::data-payload ::data-payload/data-payload)
(spec/def ::viewmodel ::viewmodel/viewmodel)

(spec/def ::course ::course/course)
(spec/def ::courses (spec/* ::course/course))
(spec/def ::checkpoint (spec/* ::checkpoint/checkpoint))

(spec/def ::auth-token string?)
(spec/def ::credentials (spec/keys :req-un [::auth-token]))

(spec/def ::event-payload (spec/or :viewmodel ::viewmodel/viewmodel
                                   :credentials ::credentials
                                   :data ::data-payload/data-payload
                                   :query ::query/query))


(defn create-tuple-spec [valid-types payload]
  (spec/tuple (into #{} valid-types) payload))

(spec/def ::event (create-tuple-spec [:found :not-found :requested
                                :rendered :refreshed] ::event-payload))

(spec/def ::Records ::event/Records)
(spec/def ::meta (spec/keys :req-un [::spec]))

(spec/def ::single-or-multiple? (spec/or :single map?
                                         :multiple (spec/* map?)))

(spec/def ::command-payload (spec/or :viewmodel ::viewmodel/viewmodel
                                     :credentials ::credentials
                                     :courses (spec/* ::course/course)
                                     :course ::course/course))

(spec/def ::command (create-tuple-spec [:update :add] ::command-payload))

(spec/def ::map-type (spec/or :keywordized (spec/map-of keyword? any?)
                              :raw (spec/map-of string? any?)))

(spec/def ::appstate ::appstate/appstate)
