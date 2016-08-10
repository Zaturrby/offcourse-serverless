(ns services.elastic-search.fetch-request
  (:require [cljs.nodejs :as node]
            [cljs.core.async :as async :refer [<! chan]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def endpoint-url (.. js/process -env -ELASTICSEARCH_ENDPOINT))
(def ^:private js-request (node/require "request"))

(defn request [url-or-opts]
  (let [c-resp (chan 1)]
    (js-request (clj->js url-or-opts)
                (fn [error response body]
                  (async/put! c-resp
                              (if error
                                {:error error}
                                {:response response :body body})
                              #(async/close! c-resp))))
    c-resp))

(defn fetch [query]
  (go
    (:body (<! (request {:url  (str "http://" endpoint-url "/offcourse/courses/_search")
                          :body (.stringify js/JSON (clj->js query))})))))
