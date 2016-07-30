(ns app.specs.index
  (:require [cljs.spec :as spec]
            [app.specs.course :as course]))

(spec/def ::type (spec/or :raw string? :proper keyword?))

(spec/def ::payload (spec/or :new-course (spec/keys :req-un [::type ::course/new-course])
                             :course (spec/keys :req-un [::type ::course/course])
                             :courses (spec/keys :req-un [::type ::course/courses])))

(spec/def ::courses ::course/courses)

(spec/def ::action (spec/keys :req-un [::payload ::type]))
(spec/def ::actions (spec/* ::action))

(spec/def ::Records (spec/* map?))

(spec/def ::kinesis-event (spec/keys :req-un [::Records]))
(spec/def ::api-event map?)

(spec/def ::event (spec/or :kinesis ::kinesis-event :api ::api-event))
