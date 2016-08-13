(ns offcourse.api.queryable
  (:require [cljs.core.async :refer [<!]]
            [cljs.core.match :refer-macros [match]]
            [offcourse.protocols.queryable :as qa]
            [offcourse.protocols.responsive :as ri]
            [protocols.convertible.index :as cv]
            [protocols.validatable :as va])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn fetch [{:keys [repositories] :as api} payload]
  (let [query-type (va/resolve-type payload)
        outgoing-event {:type :requested-data
                        :payload payload}]
    (doseq [{:keys [resources] :as repository} repositories]
      (when (contains? resources query-type)
        (go
          (let [response (<! (qa/fetch repository outgoing-event))
                incoming-event (cv/to-event response)]
            (match incoming-event
                   {:type _ :payload pl} (ri/respond api {:type :found-data
                                                          :payload pl})
                   _ (println "not found data"))))))))
