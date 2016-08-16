(ns shared.protocols.actionable
  (:require [shared.specs.core :as specs]
            [cljs.spec :as spec]
            [shared.models.action.index :as action]))

(defprotocol Actionable
  (-act [this action]))

(spec/fdef act
           :args (spec/cat :datastore any?
                           :action ::specs/action))
(defn act
  "performs an action with (possible) side-effects on a datastore"
  [this action]
  (-act this (action/create action)))
