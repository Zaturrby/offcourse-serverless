(ns offcourse.models.payload.index
  (:require [services.logger :as logger]))

(defrecord Payload [])

(defmulti new (fn [type result] type))

(defmethod new :default [type result]
  (logger/log type result)
  (map->Payload {type result}))
