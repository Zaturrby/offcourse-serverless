(ns offcourse.router.responsive
  (:require [bidi.bidi :as bidi]
            [shared.protocols.responsive :as ri]
            [pushy.core :as pushy]
            [shared.models.viewmodel.index :as viewmodel]
            [services.logger :as logger]))

(defn handle-request [rt {:keys [handler route-params]}]
  (logger/log handler)
  (logger/log route-params)
  (ri/respond rt [:requested (viewmodel/create handler route-params)]))

(defn restart [{:keys [history] :as rt}]
  (pushy/replace-token! history "/"))

(defn listen [{:keys [routes responses] :as rt}]
  (let [history (pushy/pushy (partial handle-request rt)
                             (partial bidi/match-route routes))
        rt (assoc rt :history history)]
    (pushy/start! history)
    (ri/listen rt)))

(defn mute [{:keys [history listeners] :as rt}]
  (pushy/stop! history)
  (dissoc rt :listeners))
