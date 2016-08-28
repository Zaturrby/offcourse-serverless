(ns shared.specs.viewmodel
  (:require [cljs.spec :as spec]
            [shared.specs.base :as base]))

(spec/def :vm/collection (spec/keys :req-un [::base/collection-type ::base/collection-name]))
(spec/def :vm/course     (spec/keys :req-un [::base/course-slug ::base/curator]))
(spec/def :vm/checkpoint (spec/keys :req-un [::base/checkpoint-slug]))
(spec/def ::collection   (spec/keys :req-un [:vm/collection]))
(spec/def ::checkpoint   (spec/keys :req-un [:vm/course]
                                    :opt-un [:vm/checkpoint]))
(spec/def ::viewmodel    (spec/or :collection ::collection
                                  :checkpoint ::checkpoint))
