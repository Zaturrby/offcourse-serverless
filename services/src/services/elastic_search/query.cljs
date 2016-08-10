(ns services.elastic-search.query
  (:require [cljs.spec :as spec]
            [specs.core :as specs]))

(defn query [subquery]
  {:query {:bool subquery}})

(defmulti to-es-query (fn [raw-query] (first (spec/conform ::specs/query raw-query))))

(defmethod to-es-query :course [course]
  (let [{:keys [course-slug curator]} course]
    (query {:must [{:match {:course-slug course-slug}}
                   {:match {:curator curator}}]})))

(defmethod to-es-query :collection [collection]
  (let [{:keys [collection-type collection-name]} collection
        query-key (case (keyword collection-type)
                    :flags :flags
                    :tags :checkpoints.tags
                    :curators :curator)]
    (query {:should [{:match {query-key collection-name}}]})))
