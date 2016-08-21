(ns offcourse.models.resource.index
  (:require [shared.protocols.validatable :as va :refer [Validatable]]
            [cljs.spec :as spec]
            [offcourse.specs.resources :as specs]))

(defrecord Resource []
  Validatable
  (-valid? [resource] (spec/valid? ::specs/resource resource)))

(defn new [{:keys [url task tags]}] (map->Resource {:url url
                                                    :tasks #{task}
                                                    :tags tags
                                                    :checkpoint-count 1}))
