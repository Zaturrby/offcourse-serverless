(ns shared.models.course.get
  (:refer-clojure :exclude [get])
  (:require [shared.specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [clojure.set :as set]
            [shared.protocols.validatable :as va]
            [cuerdas.core :as str]))

(defmulti get (fn [_ query] (va/resolve-type query)))

(defmethod get :tags [course _]
  (->> course
       :checkpoints
       (map :tags)
       (apply set/union)
       (into #{})))

(defmethod get :urls [course _]
  (->> course
       :checkpoints
       (map :url)
       (into #{})))

(defmethod get :checkpoint [{:keys [checkpoints]} {:keys [checkpoint-slug task checkpoint-id] :as q}]
  (if checkpoint-slug
    (->> checkpoints (some #(if (= (:checkpoint-slug %) checkpoint-slug) %)))
    (first checkpoints)))



