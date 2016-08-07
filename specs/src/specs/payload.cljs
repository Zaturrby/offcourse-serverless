(ns specs.payload
  (:require [cljs.spec :as spec]
            [specs.course :as course]
            [specs.new-course :as new-course]))

(spec/def ::course (spec/or :course ::course/course
                            :new-course ::new-course/new-course))

(spec/def ::payload (spec/or :course ::course))
