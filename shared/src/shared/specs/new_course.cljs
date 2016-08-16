(ns shared.specs.new-course
  (:require [cljs.spec :as spec]
            [specs.course :as course]
            [specs.checkpoint :as checkpoint]))

(spec/def ::checkpoints (spec/* ::checkpoint/new-checkpoint))

(spec/def ::course (spec/keys :req-un [::course/curator
                                       ::course/goal
                                       ::checkpoints]))
