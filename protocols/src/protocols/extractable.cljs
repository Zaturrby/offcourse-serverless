(ns protocols.extractable)

(defprotocol Extractable
  (-extract [this]))

(defn extract
  "Extracts data from foreign data structures, such as api reponses."
  [this]
  (-extract this))
