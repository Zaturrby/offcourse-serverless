(ns specs.query
  (:require [cljs.spec :as spec]))

(spec/def ::collection-name string?)
(spec/def ::collection-type string?)
(spec/def ::course-slug string?)
(spec/def ::curator string?)

(spec/def ::collection-query (spec/keys :req-un [::collection-type ::collection-name]))
(spec/def ::course-query (spec/keys ::req-un [::course-slug ::curator]))


(spec/def ::query (spec/or :collection ::collection-query
                           :course ::course-query))
