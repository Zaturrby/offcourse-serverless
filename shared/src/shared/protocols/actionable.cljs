(ns shared.protocols.actionable
  (:require [shared.specs.core :as specs]
            [cljs.spec :as spec]
            [shared.models.action.index :as action]))

(defprotocol Actionable
  (-perform [this action]))

(spec/fdef perform
           :args (spec/cat :datastore any?
                           :action ::specs/action))
(defn perform
  "performs an action with (possible) side-effects on a datastore"
  [this action] (-perform this (action/create action)))
