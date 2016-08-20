(ns offcourse.adapters.aws.queryable
  (:require [ajax.core :refer [POST]]
            [cljs.core.async :refer [chan]]
            [shared.models.event.index :as event])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [res]
  (let [{:keys [type payload]} (js->clj (.parse js/JSON res) :keywordize-keys true)]
    (event/create [:aws (keyword type) payload])))

(defn fetch [{:keys [endpoint]} {:keys [auth-token] :as query}]
  (let [c (chan)]
    (POST endpoint
        {:headers {:Authorization (str "Bearer " auth-token)}
         :params (clj->js query)
         :format :json
         :handler #(go (>! c (handle-response %)))})
    c))
