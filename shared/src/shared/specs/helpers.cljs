(ns shared.specs.helpers
  (:require [cljs.spec :as spec]))

(defn tuple-spec [valid-types payload]
  (spec/tuple (into #{} valid-types) payload))
