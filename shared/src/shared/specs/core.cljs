(ns shared.specs.core
  (:require [cljs.spec :as spec]
            [shared.specs.event :as event]
            [shared.specs.query :as query]
            [shared.specs.action :as action]
            [shared.specs.payload :as payload]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.appstate :as appstate]
            [shared.specs.course :as course]
            [shared.specs.helpers :as helpers]))

(spec/def ::query ::query/query)
(spec/def ::payload ::payload/payload)
(spec/def ::viewmodel ::viewmodel/viewmodel)

(spec/def ::event ::event/event)
(spec/def ::action ::action/action)

(spec/def ::appstate ::appstate/appstate)
(spec/def ::course ::course/course)

(spec/def ::spec spec/spec?)
(spec/def ::meta (spec/keys :req-un [::spec]))

(spec/def ::single-or-multiple? (spec/or :single map?
                                         :multiple (spec/* map?)))

(spec/def ::map-type (spec/or :keywordized (spec/map-of keyword? any?)
                              :raw (spec/map-of string? any?)))
