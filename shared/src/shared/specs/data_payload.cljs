(ns shared.specs.data-payload
  (:require [cljs.spec :as spec]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.course :as course]))

(spec/def ::data-payload (spec/or :viewmodel ::viewmodel/viewmodel
                                  :courses (spec/* ::course/course)
                                  :course ::course/course))
