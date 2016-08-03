(ns specs.course
  (:require [cljs.spec :as spec]))

(defn user-name-length [str]
  (>= (count str) 3))

(spec/def ::user-name (spec/or :raw (spec/and string? #(user-name-length %))
                               :proper (spec/and keyword? #(user-name-length (name %)))))

(spec/def ::course-id string?)
(spec/def ::curator ::user-name)
(spec/def ::base-id ::course-id)
(spec/def ::goal string?)
(spec/def ::course-slug string?)
(spec/def ::flags (spec/or :raw (spec/* string?)
                           :proper set?))
(spec/def ::version (spec/* int?))
(spec/def ::revision int?)
(spec/def ::forked-from (spec/or :course-id ::course-id :root nil))

(spec/def ::forks (spec/or :empty empty?
                           :raw (spec/* ::course-id)
                           :proper (spec/and set? (spec/* ::course-id))))

(spec/def ::course (spec/keys :req-un [::course-id
                                       ::base-id
                                       ::course-slug
                                       ::curator
                                       ::flags
                                       ::goal
                                       ::version
                                       ::revision
                                       ::forks
                                       ::forked-from]))

(spec/def ::new-course (spec/keys :req-un [::curator
                                           ::goal]))

(spec/def ::courses (spec/* ::course))
