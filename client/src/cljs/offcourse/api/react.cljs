(ns offcourse.api.react
  (:require [cljs.core.async :refer [<!]]
            [cljs.core.match :refer-macros [match]]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [shared.protocols.responsive :as ri]
            [shared.protocols.convertible :as cv]
            [shared.protocols.validatable :as va]
            [shared.specs.core :as specs]
            [services.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(spec/fdef react
           :args (spec/cat :component any? :event ::specs/event))

(defn react [{:keys [component-name repositories] :as api} [type query]]
  (let [query-type (va/resolve-type query)
        outgoing-event [component-name :request query]]
    (doseq [{:keys [resources] :as repository} repositories]
       (when (contains? resources query-type)
        (go
          (let [response (<! (ri/send repository outgoing-event))
                data-payload (-> response cv/to-models)]
            (match response
                   [:found-data _] (ri/respond api [:found data-payload])
                   [:error _] (ri/respond api [:not-found query])
                   _ (ri/respond api [:failed query]))))))))

(stest/instrument `react)
