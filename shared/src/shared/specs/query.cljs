(ns shared.specs.query
  (:require [cljs.spec :as spec]
            [shared.specs.appstate :as appstate]))

(spec/def ::collection-name string?)
(spec/def ::collection-type string?)

(spec/def ::course-slug string?)
(spec/def ::curator string?)
(spec/def ::checkpoint-slug string?)

(spec/def ::collection (spec/keys :req-un [::collection-type ::collection-name]))
(spec/def ::course (spec/keys :req-un [::course-slug ::curator]))
(spec/def ::checkpoint (spec/keys :req-un [::checkpoint-slug]))

(spec/def ::tags #{:all})
(spec/def ::urls #{:all})
(spec/def ::url string?)
(spec/def ::tags-query (spec/keys :req-un [::tags]))

(spec/def ::urls-query (spec/keys :req-un [::urls]))
(spec/def ::url-query (spec/keys :req-un [::url]))

(spec/def ::query (spec/or :collection ::collection
                           :tags ::tags-query
                           :urls ::urls-query
                           :resource ::url-query
                           :checkpoint ::checkpoint
                           :appstate ::appstate/appstate
                           :courses (spec/* ::course)
                           :course ::course))
