(ns shared.models.course.get
  (:refer-clojure :exclude [get])
  (:require [shared.specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [clojure.set :as set]
            [shared.protocols.validatable :as va]
            [cuerdas.core :as str]
            [services.logger :as logger]))

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

(defmethod get :checkpoint [course checkpoint]
  (if-let [checkpoint-slug (or (:checkpoint-slug checkpoint) (str/slugify (:task checkpoint)))]
    (do
      (->> (:checkpoints course)
           (some #(if (= (:checkpoint-slug %) checkpoint-slug) %))))
    (->> (:checkpoints course)
         (some #(if (= (:checkpoint-id %) (:checkpoint-id checkpoint)) %)))))


