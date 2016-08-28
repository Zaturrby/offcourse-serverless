(ns shared.specs.query
  (:require [cljs.spec :as spec]
            [shared.specs.base :as base]
            [shared.specs.appstate :as appstate]
            [shared.specs.viewmodel :as viewmodel]))

(spec/def ::checkpoint-slug (spec/nilable ::base/checkpoint-slug))
(spec/def ::collection      (spec/keys :req-un [::base/collection-type ::base/collection-name]))
(spec/def ::course          (spec/keys :req-un [::base/course-slug ::base/curator]))
(spec/def ::checkpoint      (spec/keys :req-un [::checkpoint-slug]))
(spec/def ::tags            (spec/keys :req-un [::base/tags]))
(spec/def ::urls            (spec/keys :req-un [::base/urls]))
(spec/def ::url             (spec/keys :req-un [::base/url]))
(spec/def ::user-profile    (spec/keys :req-un [::base/user-profile]))

(spec/def ::query (spec/or :collection ::collection
                           :tags ::tags
                           :urls ::urls
                           :resource ::url
                           :user-profile ::user-profile
                           :viewmodel ::viewmodel/viewmodel
                           :checkpoint ::checkpoint
                           :course ::course))
