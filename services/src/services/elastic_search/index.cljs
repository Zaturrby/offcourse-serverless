(ns services.elastic-search.index
  (:require [services.elastic-search.fetch :as fetch]
            [services.elastic-search.save :as save]))

(def save save/save)
(def fetch fetch/fetch)
