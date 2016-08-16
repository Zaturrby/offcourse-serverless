(ns shared.specs.core
  (:require [cljs.spec :as spec]
            [shared.specs.event :as event]
            [shared.specs.payload :as payload]
            [shared.specs.query :as query]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.course :as course]))

(spec/def ::type #{:requested-save
                   :requested-data
                   :found-data})

(spec/def ::valid-payload ::payload/valid-payload)
(spec/def ::payload ::payload/payload)
(spec/def ::action (spec/keys :req-un [::payload ::type]))
(spec/def ::spec spec/spec?)


(spec/def ::data-payload ::payload/data-payload)
(spec/def ::query ::query/query)
(spec/def ::viewmodel ::viewmodel/viewmodel)

(spec/def ::course ::course/course)
(spec/def ::courses (spec/* ::course/course))

(spec/def ::event ::event/event)
(spec/def ::Records ::event/Records)
(spec/def ::meta (spec/keys :req-un [::spec]))

(spec/def ::single-or-multiple? (spec/or :single map?
                                         :multiple (spec/* map?)))

(spec/def ::action (spec/tuple keyword? (spec/or :data ::data-payload
                                                 :query ::query
                                                 :viewmodel ::viewmodel)))

(spec/def ::map-type (spec/or :keywordized (spec/map-of keyword? any?)
                              :raw (spec/map-of string? any?)))
