(ns shared.specs.course
  (:require [cljs.spec :as spec]
            [shared.specs.checkpoint :as checkpoint]
            [shared.specs.base :as base]))

(spec/def ::course-id string?)
(spec/def ::goal string?)
(spec/def ::version (spec/* int?))
(spec/def ::revision int?)
(spec/def ::forked-from (spec/or :course-id ::course-id :root nil))
(spec/def ::forks (spec/* ::course-id))

(spec/def checkpoints ::checkpoint/new-checkpoints)

(spec/def ::new-course (spec/keys :req-un [::base/curator
                                           ::goal
                                           ::checkpoints]))

(spec/def ::course (spec/keys :req-un [::course-id
                                       ::base-id
                                       ::base/curator
                                       ::base/flags
                                       ::goal
                                       ::version
                                       ::revision
                                       ::checkpoint/checkpoints]
                              :opt-un [::forks
                                       ::forked-from]))


(spec/def ::courses (spec/* ::course))
