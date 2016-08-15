(ns protocols.validatable
  (:require [cljs.spec.test :as stest]
            [cljs.spec :as spec]))

(stest/instrument)

(defprotocol Validatable
  (-missing-data [this] [this query])
  (-resolve-type [this])
  (-errors [this])
  (-valid? [this]))

(extend-type object
  Validatable
  (-valid? [this]
    (when-let [{:keys [spec]} (meta this)]
      (spec/valid? spec this)))
  (-missing-data
    ([this] (-missing-data this))
    ([this query] (-missing-data this query)))
  (-errors [this]
    (when-let [{:keys [spec]} (meta this)]
      (spec/explain spec this)))
  (-resolve-type [this]
    (when-let [{:keys [spec]} (meta this)]
      (first (spec/conform spec this)))))


(defn missing-data
  "If given one argument, this function explains what data needs to be provided
  in order for it to comply with its specification. With two arguments, it
  explains what data is missing in order to meet the given query"
  ([this]
   (-missing-data this))
  ([this query]
   (-missing-data this query)))

(spec/fdef missing-data :ret boolean?)

(defn valid?
  "Resolves the type of this given object based on its specification"
  [this]
  (-valid? this))

(defn resolve-type
  "Tells why a given object does not comply with its specification"
  [this]
  (-resolve-type this))

(defn errors
  "Checks if a given object complies with its specification"
  [this]
  (-errors this))
