(ns shared.specs.appstate
  (:require [cljs.spec :as spec]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.base :as base]))


(spec/def ::appstate (spec/keys :req-un [::base/site-title ::viewmodel/viewmodel]
                                :opt-un [::base/auth-token ::base/user]))
