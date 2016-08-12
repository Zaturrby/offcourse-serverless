(ns offcourse.api.queryable
  (:require [cljs.core.async :refer [<!]]
            [cljs.core.match :refer-macros [match]]
            [cljs.spec :as spec]
            [offcourse.protocols.queryable :as qa]
            [offcourse.protocols.responsive :as ri]
            [protocols.validatable :as va]
            [specs.core :as specs])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn fetch [{:keys [repositories] :as api} query]
  (let [query-type (va/resolve-type query)]
    (doseq [{:keys [resources] :as repository} repositories]
      (when (contains? resources query-type)
        (go
          (let [response (<! (qa/fetch repository {:type :requested-data
                                                   :payload query}))]
            (match response
                   {:type _} (ri/respond api (update-in response [:type] #(keyword %)))
                   _ (println "not found data"))))))))
