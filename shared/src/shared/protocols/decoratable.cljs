(ns shared.protocols.decoratable)

(defprotocol Decoratable
  (-decorate [this] [this appstate] [this user-name slug]))

(defn decorate
  ([this] (-decorate this))
  ([this appstate] (-decorate this appstate))
  ([this user-name slug] (-decorate this user-name slug)))
