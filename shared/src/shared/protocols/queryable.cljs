(ns shared.protocols.queryable
  (:refer-clojure :exclude [get -reset remove])
  (:require [shared.models.query.index :as query]))

(defprotocol Queryable
  (-search     [this query])
  (-get     [this query])
  (-check   [this] [this query])
  (-fetch   [this query])
  (-missing-data [this query])
  (-add     [this query])
  (-remove  [this query])
  (-refresh [this] [this doc]))

(defn search
  "Given a query, this command will return the requested data synchronously
  from the queried object"
  [this query] (-search this (query/create query)))

(defn get
  "Given a query, this command will return the requested data synchronously
  from the queried object"
  [this query] (-get this (query/create query)))

(defn fetch
  "Given a query, this command will return the requested data asynchronously
  on a channel"
  ([this query] (-fetch this (query/create query))))

(defn refresh
  ([this query] (-refresh this (query/create query))))

(defn missing-data
  "If given one argument, this function explains what data needs to be provided
  in order for it to comply with its specification. With two arguments, it
  explains what data is missing in order to meet the given query"
  ([this query] (-missing-data this (query/create query))))

(defn add
  "Given a query, this command will add the requested data to the queried object"
  ([this query] (-add this (query/create query))))

(defn remove
  "Given a query, this command will remove the requested data from the queried
  object"
  ([this query] (-remove this (query/create query))))

(defn check
  "Given a query, this check if the requested data is present in the queried
  object"
  ([this query] (-check this query)))
