(ns specs.query
  (:require [cljs.spec :as spec]))

(spec/def ::collection-name string?)
(spec/def ::collection-type string?)
(spec/def ::course-slug string?)
(spec/def ::curator string?)
(spec/def ::loading empty?)

(spec/def ::collection (spec/keys :req-un [::collection-type ::collection-name]))
(spec/def ::course (spec/keys :req-un [::course-slug ::curator]))

(spec/def ::collection-view (spec/keys :req-un [::collection]))
(spec/def ::loading-view (spec/keys :req-un [::loading]))

(spec/def ::viewmodel (spec/or :collection ::collection-view
                               :loading ::loading-view))

(spec/def ::tags #{:all})
(spec/def ::tags-query (spec/keys :req-un [::tags]))

(spec/def ::query (spec/or :collection ::collection
                           :tags ::tags-query
                           :courses (spec/* ::course)
                           :course ::course))
