(ns offcourse.api.queryable
  (:require [cljs.core.async :refer [<!]]
            [cljs.core.match :refer-macros [match]]
            [offcourse.protocols.queryable :as qa]
            [offcourse.protocols.responsive :as ri]
            [offcourse.protocols.loggable :as la]
            [protocols.convertible.index :as cv]
            [services.logger :as logger]
            [models.data-payload.index :as data-payload]
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
                data-payload (-> response cv/to-event cv/to-models)]
            (match response
                   {:type _ :payload _} (ri/respond api {:type :found-data
                                                         :payload (data-payload/create data-payload)})
                   _ (println "not found data"))))))))
