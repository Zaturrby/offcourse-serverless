(ns app.specs.index
  (:require [cljs.spec :as spec]
            [app.specs.course :as course]))

(spec/def ::payload (spec/or :course ::course/course))
(spec/def ::type (spec/or :raw string? :proper keyword?))
(spec/def ::action (spec/keys :req-un [::payload ::type]))
