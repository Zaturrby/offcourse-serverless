(ns specs.core
  (:require [cljs.spec :as spec]
            [specs.event :as event]
            [specs.payload :as payload]
            [specs.course :as course]))

(spec/def ::type (spec/or :raw string? :proper keyword?))

(spec/def ::data-types (spec/or :courses ::course/courses
                                :course ::course/course))

(spec/def ::payload-var (spec/cat :type ::type :payload-data ::data-types))

(spec/def ::payload ::payload/payload)

(spec/def ::action (spec/keys :req-un [::payload ::type]))

(spec/def ::event ::event/event)
