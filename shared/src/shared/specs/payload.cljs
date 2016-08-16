(ns shared.specs.payload
  (:require [cljs.spec :as spec]
            [specs.course :as course]
            [specs.query :as query]
            [specs.new-course :as new-course]))

(spec/def ::raw-payload (spec/or :course ::new-course/course
                                 :courses (spec/* ::new-course/course)))

(spec/def ::valid-payload (spec/or :course ::course/course
                                   :courses (spec/* ::course/course)))

(spec/def ::data-payload (spec/or :course ::course/course
                                  :courses (spec/* ::course/course)))

(spec/def ::query-payload ::query/query)

(spec/def ::payload (spec/or :raw ::raw-payload
                             :valid ::valid-payload
                             :query ::query-payload))
