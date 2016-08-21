(ns shared.models.course.search
  (:require [shared.specs.core :as specs]
            [shared.protocols.queryable :refer [Queryable]]
            [clojure.set :as set]
            [shared.protocols.validatable :as va]
            [cuerdas.core :as str]
            [services.logger :as logger]))

(defmulti search (fn [_ query] (va/resolve-type query)))

(defmethod search :tags [course _]
  (->> course
       :checkpoints
       (map :tags)
       (apply set/union)
       (into #{})))

(defmethod search :urls [course _]
  (->> course
       :checkpoints
       (map :url)
       (into #{})))

(defmethod search :checkpoint [course checkpoint]
  (if-let [checkpoint-slug (or (:checkpoint-slug checkpoint) (str/slugify (:task checkpoint)))]
    (do
      (->> (:checkpoints course)
           (some #(if (= (:checkpoint-slug %) checkpoint-slug) %))))
    (->> (:checkpoints course)
         (some #(if (= (:checkpoint-id %) (:checkpoint-id checkpoint)) %)))))


