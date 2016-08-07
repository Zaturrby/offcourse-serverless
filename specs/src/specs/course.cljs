(ns specs.course
  (:require [cljs.spec :as spec]
            [specs.checkpoint :as checkpoint]))


(defn user-name-length [str]
  (>= (count str) 3))

(spec/def ::user-name (spec/and string? #(user-name-length %)))

(spec/def ::course-id string?)
(spec/def ::curator ::user-name)
(spec/def ::base-id ::course-id)
(spec/def ::goal string?)
(spec/def ::course-slug string?)
(spec/def ::flags (spec/* string?))
(spec/def ::version (spec/* int?))
(spec/def ::revision int?)
(spec/def ::forked-from (spec/or :course-id ::course-id :root nil))

(spec/def ::forks (spec/* ::course-id))

(spec/def ::checkpoints (spec/* ::checkpoint/checkpoint))

(spec/def ::course (spec/keys :req-un [::course-id
                                       ::base-id
                                       ::course-slug
                                       ::curator
                                       ::flags
                                       ::goal
                                       ::version
                                       ::revision
                                       ::forks
                                       ::forked-from
                                       ::checkpoints]))

(spec/def ::courses (spec/* ::course))
