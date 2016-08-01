(ns app.notify
  (:require [cljs.nodejs :as node]
            [app.specs.index :as specs]
            [app.action :as action]
            [app.event :as event]
            [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [cljs.core.async :refer [<! put! close! chan >!]]
            [app.logger :as logger])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def ^:private -request (node/require "request"))

(defn- request [options]
  (let [c-resp (chan 1)]
    (.post -request (clj->js options)
                (fn [error response body]
                  (put! c-resp
                        (if error
                          {:error error}
                          {:response response :body body})
                        #(close! c-resp))))
    c-resp))

(defmulti notify (fn [type payload] type))

(defmethod notify :slack [_ {:keys [type] :as payload}]
  (go
    (let [url "https://hooks.slack.com/services/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"
          {:keys [curator goal course-slug]} (first (type payload))
          message (str curator " has just posted a course on: \"" goal  "\". Check it out here:  <http://staging.offcourse.io/"  curator "/courses/" course-slug ">.")
          body (.stringify js/JSON (clj->js {:text message}))]
      (println body)
      (println (<! (request {:url url
                             :body body}))))))
