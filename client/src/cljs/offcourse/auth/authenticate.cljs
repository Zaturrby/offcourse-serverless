(ns offcourse.auth.authenticate
  (:require [cljs.core.async :as async :refer [<! >! chan]]
            [shared.protocols.responsive :as ri])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn -sign-in [provider]
  (let [c (chan)]
    (.show provider (fn [error response token]
                      (async/put! c {:error error
                                     :response response
                                     :token token})))
    c))

(defn -sign-out [] (.removeItem js/localStorage "auth-token"))

(defn sign-in [{:keys [config provider] :as auth}]
  (go
    (let [{:keys [token]} (<! (-sign-in provider))]
      (.setItem js/localStorage "auth-token" token)
      (ri/respond auth [:granted {:auth-token token}]))))

(defn sign-out [auth]
  (go
    (-sign-out)
    (ri/respond auth [:revoked {:auth-token nil}])))
