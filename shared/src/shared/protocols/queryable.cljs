(ns shared.protocols.queryable
  (:refer-clojure :exclude [get -reset remove])
  (:require [shared.models.query.index :as query]))

(defprotocol Queryable
  (-get     [this query])
  (-add     [this query])
  (-remove  [this query])
  (-check   [this] [this query])
  (-fetch   [this query])
  (-refresh [this] [this doc]))

(defn get
  "Given a query, this command will return the requested data synchronously from the queried object"
  [this query] (-get this (query/create query)))

(defn fetch
  "Given a query, this command will return the requested data asynchronously from the queried object"
  ([this query] (-fetch this (query/create query))))

(defn refresh
  ([this query] (-refresh this (query/create query))))

(defn add
  "Given a query, this command will add the requested data to the queried object"
  ([this query] (-add this (query/create query))))

(defn remove
  "Given a query, this command will remove the requested data from the queried object"
  ([this query] (-remove this (query/create query))))

(defn check
  "Given a query, this check if the requested data is present in the queried object"
  ([this query] (-check this query)))
