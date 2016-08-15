(ns protocols.queryable
  (:refer-clojure :exclude [get -reset remove])
  (:require [models.query.index :as query]))

(defprotocol Queryable
  (-get     [this query]))

(defn get
  "Given a query, this command will return the requested data"
  [this query] (-get this (query/create query)))
