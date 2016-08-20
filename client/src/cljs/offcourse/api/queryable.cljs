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
           :args (spec/cat :component any? :event ::specs/event))

(defn fetch [{:keys [repositories] :as api} [type query]]
  (let [query-type (va/resolve-type query)
        outgoing-event {:type :requested-data
                        :payload query}]
    (doseq [{:keys [resources] :as repository} repositories]
       (when (contains? resources query-type)
        (go
          (let [response (<! (qa/fetch repository outgoing-event))
                data-payload (-> response cv/to-models)]
            (match response
                   [:found-data _] (ri/respond api [:found data-payload])
                   [:error _] (ri/respond api [:not-found query])
                   _ (ri/respond api [:failed query]))))))))

(stest/instrument `fetch)
