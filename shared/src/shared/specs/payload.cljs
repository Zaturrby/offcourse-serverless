(ns shared.specs.payload
  (:require [cljs.spec :as spec]
            [shared.specs.course :as course]
            [shared.specs.appstate :as appstate]
            [shared.specs.viewmodel :as viewmodel]))


(spec/def ::payload (spec/or :courses ::course/courses
                             :course ::course/course
                             :new-course ::course/new-course
                             :new-courses (spec/* ::course/new-course)
                             :appstate ::appstate/appstate
                             :viewmodel ::viewmodel/viewmodel))
