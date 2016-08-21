(ns offcourse.protocols.queryable
  (:refer-clojure :exclude [get -reset remove]))

(defprotocol Queryable
  (-fetch   [this query]))

(defn fetch
  ([this query] (-fetch this query)))
