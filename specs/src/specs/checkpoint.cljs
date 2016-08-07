(ns specs.checkpoint
  (:require [cljs.spec :as spec]))

(spec/def ::task string?)
(spec/def ::url string?)
(spec/def ::checkpoint-id int?)
(spec/def ::checkpoint-slug string?)

(spec/def ::completed? (spec/or :true int? :false #(= false %)))

(spec/def ::new-checkpoint (spec/and (spec/keys :req-un [::task ::url])
                                     #(not (:checkpoint-id %))))

(spec/def ::checkpoint (spec/keys :req-un [::task ::url ::checkpoint-id]
                                  :opt-un [::completed?]))
