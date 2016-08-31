(ns shared.protocols.decoratable)

(defprotocol Decoratable
  (-decorate [this] [this appstate] [this user-name slug] [this user-name slug routes]))


(defn decorate
  ([this] (-decorate this))
  ([this appstate] (-decorate this appstate))
  ([this user-name slug] (-decorate this user-name slug))
  ([this user-name slug routes] (-decorate this user-name slug routes)))
