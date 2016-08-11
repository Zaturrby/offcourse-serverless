(ns offcourse.api.queryable
  (:require [cljs.core.async :refer [<!]]
            [offcourse.protocols.convertible :as ci]
            [offcourse.protocols.queryable :as qa]
            [cljs.core.match :refer-macros [match]]
            [offcourse.protocols.responsive :as ri]
            [cljs.spec :as spec]
            [specs.core :as specs]
            [offcourse.models.payload.index :as payload])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn fetch [{:keys [repositories] :as api} query]
  (doseq [{:keys [resources] :as repository} repositories]
    (when (contains? resources (first (spec/conform ::specs/query query)))
      (go
        (let [response (<! (qa/fetch repository {:type :requested-data
                                                 :payload query}))]
          (match response
                 {:type _}  (ri/respond api (update-in response [:type] #(keyword %)))
                 _ (println "not found data")))))))
