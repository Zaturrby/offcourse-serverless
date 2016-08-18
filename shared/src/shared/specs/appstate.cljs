(ns shared.specs.appstate
  (:require [cljs.spec :as spec]
            [shared.specs.viewmodel :as viewmodel]))

(spec/def ::site-title string?)

(spec/def ::user-name string?)
(spec/def ::user (spec/nilable (spec/keys :req-un [::user-name])))

(spec/def ::appstate (spec/keys :req-un [::site-title ::user ::viewmodel/viewmodel]))
