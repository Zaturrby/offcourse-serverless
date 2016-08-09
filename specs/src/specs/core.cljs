(ns specs.core
  (:require [cljs.spec :as spec]
            [specs.event :as event]
            [specs.payload :as payload]
            [specs.course :as course]))

(spec/def ::type #{:requested-save})

(spec/def ::valid-payload ::payload/valid-payload)
(spec/def ::payload ::payload/payload)
(spec/def ::action (spec/keys :req-un [::payload ::type]))

(spec/def ::event ::event/event)
(spec/def ::Records ::event/Records)

(spec/def ::single-or-multiple? (spec/or :single map?
                                         :multiple (spec/* map?)))
