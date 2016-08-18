(ns shared.specs.viewmodel
  (:require [cljs.spec :as spec]))

(spec/def ::collection-name string?)
(spec/def ::collection-type string?)

(spec/def ::course-slug string?)
(spec/def ::curator string?)

(spec/def :vm/collection (spec/keys :req-un [::collection-type ::collection-name]))
(spec/def :vm/course (spec/keys :req-un [::course-slug ::curator]))

(spec/def :vm/loading empty?)

(spec/def ::collection (spec/keys :req-un [:vm/collection]))
(spec/def ::course (spec/keys :req-un [:vm/course]))
(spec/def ::loading (spec/keys :req-un [:vm/loading]))

(spec/def ::viewmodel (spec/or :collection ::collection
                                :course ::course
                                :loading ::loading))
