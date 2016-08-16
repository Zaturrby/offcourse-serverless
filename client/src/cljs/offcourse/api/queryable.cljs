(ns offcourse.api.queryable
  (:require [cljs.core.async :refer [<!]]
            [cljs.core.match :refer-macros [match]]
            [offcourse.protocols.queryable :as qa]
            [offcourse.protocols.responsive :as ri]
            [offcourse.protocols.loggable :as la]
            [shared.protocols.convertible :as cv]
            [services.logger :as logger]
            [shared.models.data-payload.index :as data-payload]
            [shared.protocols.validatable :as va]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [cljs.spec.test :as stest])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(spec/fdef fetch
           :args (spec/cat :component any? :query ::specs/query))

(defn fetch [{:keys [repositories] :as api} query]
  (let [query-type (va/resolve-type query)
        outgoing-event {:type :requested-data
                        :payload query}]
    (doseq [{:keys [resources] :as repository} repositories]
      (when (contains? resources query-type)
        (go
          (let [response (<! (qa/fetch repository outgoing-event))
                data-payload (-> response cv/to-event cv/to-models)]
            (match response
                   {:type _ :payload _} (ri/respond api {:type :found-data
                                                         :payload (data-payload/create data-payload)})
                   _ (println "not found data"))))))))

(stest/instrument `fetch)
