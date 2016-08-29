(ns shared.specs.action
  (:require [cljs.spec :as spec]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.credentials :as credentials]
            [shared.specs.course :as course]
            [shared.specs.helpers :as helpers]))

(spec/def ::action-payload (spec/nilable (spec/or :viewmodel    ::viewmodel/viewmodel
                                                  :credentials  ::credentials/credentials
                                                  :view-actions set?
                                                  :courses      (spec/* ::course/course)
                                                  :course       ::course/course)))

(spec/def ::action (helpers/tuple-spec [:update :sign-in :add] ::action-payload))
