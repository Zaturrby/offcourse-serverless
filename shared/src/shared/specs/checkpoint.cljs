(ns shared.specs.checkpoint
  (:require [cljs.spec :as spec]
            [shared.specs.base :as base]))

(spec/def ::task string?)
(spec/def ::checkpoint-id int?)

(spec/def ::completed? (spec/or :true int? :false #(= 0 %)))

(spec/def ::new-checkpoint (spec/and (spec/keys :req-un [::task ::url])
                                     #(not (:checkpoint-id %))))

(spec/def ::checkpoint (spec/keys :req-un [::task ::base/url ::base/checkpoint-slug ::checkpoint-id]
                                  :opt-un [::completed?]))

(spec/def ::checkpoints (spec/* ::checkpoint))
