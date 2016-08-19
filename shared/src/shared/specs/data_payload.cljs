(ns shared.specs.data-payload
  (:require [cljs.spec :as spec]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.course :as course]
            [shared.specs.query :as query]))

(spec/def ::auth-token string?)
(spec/def ::auth (spec/keys :req-un [::auth-token]))
(spec/def ::data-payload (spec/or :courses (spec/* ::course/course)
                          :course ::course/course))
