(ns shared.specs.credentials
  (:require [cljs.spec :as spec]
            [shared.specs.base :as base]))

(spec/def ::credentials (spec/keys :req-un [::base/auth-token]))
