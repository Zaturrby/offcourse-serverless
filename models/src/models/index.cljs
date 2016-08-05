(ns models.index
  (:require [models.implementations :as impl]
            [protocols.convertible :refer [Convertible]]))

(extend-type object
  Convertible
  (-to-event [this] (impl/to-event this)))
