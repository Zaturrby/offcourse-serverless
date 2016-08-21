(ns shared.specs.payload
  (:require [cljs.spec :as spec]
            [shared.specs.course :as course]
            [shared.specs.query :as query]))


(spec/def ::valid-payload (spec/or :course ::course/course
                                   :courses (spec/* ::course/course)))

(spec/def ::data-payload (spec/or :course ::course/course
                                  :courses (spec/* ::course/course)))

(spec/def ::query-payload ::query/query)

(spec/def ::payload (spec/or :valid ::valid-payload
                             :query ::query-payload))
