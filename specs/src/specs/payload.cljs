(ns specs.payload
  (:require [cljs.spec :as spec]
            [specs.course :as course]))

(spec/def ::payload (spec/or :new-course (spec/keys :req-un [::type ::course/new-course])
                             :course (spec/keys :req-un [::type ::course/course])
                             :courses (spec/keys :req-un [::type ::course/courses])))
