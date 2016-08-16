(ns shared.specs.viewmodel
  (:require [cljs.spec :as spec]
            [shared.specs.query :as query]))

(spec/def ::loading empty?)

(spec/def ::collection-view (spec/keys :req-un [::query/collection]))
(spec/def ::course-view (spec/keys :req-un [::query/course]))
(spec/def ::loading-view (spec/keys :req-un [::loading]))

(spec/def ::viewmodel (spec/or :collection ::collection-view
                               :course ::course-view
                               :loading ::loading-view))

