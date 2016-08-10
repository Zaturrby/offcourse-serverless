(ns services.elastic-search.fetch
  (:require [cljs.core.async :as async :refer [<!]]
            [cljs.spec :as spec]
            [services.elastic-search.extract :as extract]
            [services.elastic-search.query :as query]
            [services.elastic-search.fetch-request :as request]
            [specs.core :as specs])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defmulti fetch (fn [event] (first (spec/conform ::specs/query event))))

(defmethod fetch :course [query]
  (go
    (let [es-query (query/to-es-query query)
          res      (<! (request/fetch es-query))
          course  (extract/course res)]
      course)))

(defmethod fetch :collection [query]
  (go
    (let [es-query (query/to-es-query query)
          res      (<! (request/fetch es-query))
          courses  (extract/courses res)]
      courses)))
