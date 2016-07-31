(ns app.specs.index
  (:require [cljs.spec :as spec]
            [app.specs.course :as course]))

(spec/def ::type (spec/or :raw string? :proper keyword?))

(defmulti payload :type)

(defmethod payload :new-course [_] (spec/keys :req-un [::type ::course/new-course]))
(defmethod payload :course [_] (spec/keys :req-un [::type ::course/course]))

(spec/def ::payload (spec/multi-spec payload :type))
(spec/def ::action (spec/keys :req-un [::payload ::type]))

(spec/def ::event (spec/or :kinesis ::kinesis-event :api ::api-event))
