(ns services.notifier
  (:require [cljs.nodejs :as node]
            [cljs.core.async :as async :refer [<! put! close! chan >!]]
            [services.logger :as logger])
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

(defn create-message [{:keys [curator goal course-slug]}]
  (str curator " has just posted a course on: \"" goal
       "\". Check it out here:  <http://staging.offcourse.io/"
       curator "/courses/" course-slug ">."))

(defn create-request [item]
  (let [url "https://hooks.slack.com/services/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"
        message (create-message item)
        body (.stringify js/JSON (clj->js {:pretext "New Course Hurray!!!"
                                           :username "yeehaabot"
                                           :icon_emoji ":offcourse:"
                                           :text message}))]
    (request {:url url :body body})))

(defmulti notify (fn [type payload] type))

(defmethod notify :slack [_ payload]
  (let [chans (async/merge (map create-request payload))]
    (async/into [] chans)))

