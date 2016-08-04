(ns specs.event
  (:require [cljs.spec :as spec]
            [specs.payload :as payload]))

(spec/def ::Records map?)
(spec/def ::type keyword?)

(spec/def ::api-event (spec/keys :req-un [::type ::payload/payload]))
(spec/def ::stream-event (spec/keys :req-un [::Records]))

(spec/def ::event (spec/or :kinesis ::stream-event :api ::api-event))
