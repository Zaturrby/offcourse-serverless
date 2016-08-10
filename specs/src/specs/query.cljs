(ns specs.query
  (:require [cljs.spec :as spec]))

(spec/def ::collection-name string?)
(spec/def ::collection-type string?)
(spec/def ::collection-query (spec/keys :req-un [::collection-type ::collection-name]))

(spec/def ::query (spec/or :collection ::collection-query))
