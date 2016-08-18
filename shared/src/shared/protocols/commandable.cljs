(ns shared.protocols.commandable
  (:require [shared.specs.core :as specs]
            [cljs.spec :as spec]
            [shared.models.command.index :as command]
            [services.logger :as logger]))

(defprotocol Commandable
  (-exec [this command]))

(spec/fdef do
           :args (spec/cat :datastore any?
                           :action ::specs/action))
(defn exec
  "performs an action with (possible) side-effects on a datastore"
  [this command] (-exec this (command/create command)))
