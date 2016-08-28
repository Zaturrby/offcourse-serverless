(ns shared.protocols.queryable
  (:refer-clojure :exclude [get -reset remove])
  (:require [shared.models.query.index :as query]))

(defprotocol Queryable
  (-get     [this query])
  (-check   [this] [this query])
  (-missing-data [this query]))

(defn get
  "Given a query, this command will return the requested data synchronously
  from the queried object"
  [this query] (-get this (query/create query)))

(defn check
  "Given a query, this check if the requested data is present in the queried
  object"
  ([this query] (-check this query)))

(defn missing-data
  "If given one argument, this function explains what data needs to be provided
  in order for it to comply with its specification. With two arguments, it
  explains what data is missing in order to meet the given query"
  ([this query] (-missing-data this (query/create query))))
