(ns shared.models.appstate.missing-data
  (:refer-clojure :exclude [get -reset remove])
  (:require [clojure.set :as set]
            [shared.models.query.index :as query]
            [shared.protocols.queryable :as qa]
            [shared.protocols.validatable :as va]
            [services.logger :as logger]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]))

(defmulti missing-data (fn [state viewmodel]
                         (first (spec/conform ::specs/viewmodel viewmodel))))

(defmethod missing-data :collection-view [state viewmodel]
  (query/create (:collection viewmodel)))

(defmethod missing-data :checkpoint-view [state viewmodel]
  (let [course-query (-> viewmodel :course)
        course (qa/get state course-query)]
    (if (:checkpoints course)
      nil
      #_(va/missing-data state (payload/new :resources (map (fn [url] {:url url})
                                                            (qa/get course {:urls :all}))))
      (query/create course-query))))

(defmethod missing-data :default [state viewmodel] nil)
