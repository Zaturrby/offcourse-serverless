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

(spec/def ::course (spec/keys :req-un [::course-id
                                       ::base-id
                                       ::base/curator
                                       ::base/flags
                                       ::goal
                                       ::version
                                       ::revision
                                       ::forks
                                       ::forked-from
                                       ::checkpoint/checkpoints]))

(spec/def ::courses (spec/* ::course))
