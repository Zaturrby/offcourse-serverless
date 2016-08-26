(ns offcourse.system.handlers
  (:require [bidi.bidi :refer [path-for]]
            [services.logger :as logger]))

(def handlers
  {:sign-in (fn [responder] (partial responder :requested-sign-in))
   :sign-out (fn [responder] (partial responder :requested-sign-out))})
