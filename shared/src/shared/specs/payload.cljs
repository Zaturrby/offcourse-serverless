(ns shared.specs.payload
  (:require [cljs.spec :as spec]
            [shared.specs.course :as course]
            [shared.specs.appstate :as appstate]))

(spec/def ::payload (spec/or :courses ::course/courses
                             :appstate ::appstate/appstate
                             :course ::course/course))
