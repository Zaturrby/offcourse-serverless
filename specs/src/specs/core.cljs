(ns specs.core
  (:require [cljs.spec :as spec]
            [specs.course :as course]))

(spec/def ::type (spec/or :raw string? :proper keyword?))

(spec/def ::data-types (spec/or :courses ::course/courses
                                :course ::course/course))

(spec/def ::payload (spec/or :new-course (spec/keys :req-un [::type ::course/new-course])
                             :course (spec/keys :req-un [::type ::course/course])
                             :courses (spec/keys :req-un [::type ::course/courses])))

(spec/def ::action (spec/keys :req-un [::payload ::type]))

(spec/def ::event (spec/or :kinesis ::kinesis-event :api ::api-event))
