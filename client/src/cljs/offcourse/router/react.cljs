(ns offcourse.router.react
  (:require [shared.protocols.convertible :as cv]
            [pushy.core :as pushy]
            [services.logger :as logger]))

(defn refresh [{:keys [history routes url-helpers responses]} [_ payload]]
  (let [{:keys [type] :as viewmodel} (-> payload :viewmodel)
        old-url (pushy/get-token history)
        new-url (cv/to-url viewmodel routes)]
    (when-not (= old-url new-url)
      (pushy/replace-token! history new-url))))
