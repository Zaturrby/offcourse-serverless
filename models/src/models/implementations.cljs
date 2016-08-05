(ns models.implementations
  (:require [models.event.index :as event]
            [services.helpers :as helpers]
            [services.logger :as logger]))

(defn to-event [raw-event]
  (logger/log "Event: " raw-event)
  (-> raw-event
      (js->clj :keywordize-keys true)
      helpers/keywordize-type
      event/map->Event))
