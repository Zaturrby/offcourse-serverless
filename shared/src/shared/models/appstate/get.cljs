(ns shared.models.appstate.get
  (:refer-clojure :exclude [get -reset remove])
  (:require [shared.models.appstate.paths :as paths]
            [shared.protocols.validatable :as va])
  (:require-macros [com.rpl.specter.macros :refer [select-first]]))

(defmulti get (fn [_ query] (va/resolve-type query)))

(defmethod get :courses [{:keys [courses] :as ds} query]
  (when courses
    (let [course-ids (map :course-id (:courses query))]
      (when-let [courses (keep #(select-first (paths/course %) ds) course-ids)]
        (if (empty? courses) nil courses)))))

(defmethod get :course [{:keys [courses] :as ds} course]
  (when courses
    (select-first (paths/course course) ds)))

(defmethod get :checkpoint [{:keys [courses] :as ds} {:keys [checkpoint]}]
  (when courses
    (select-first (paths/checkpoint checkpoint) ds)))

(defmethod get :resources [{:keys [resources] :as ds} query]
  (when resources
    (let [urls (map :url (:resources query))]
      (when-let [resources (keep #(select-first (paths/resource %) ds) urls)]
        (if (empty? resources) nil resources)))))


(defmethod get :resource [{:keys [resources] :as ds} {:keys [resource]}]
  (when resources
    (when-let [{:keys [url]} resource]
      (select-first (paths/resource url) ds))))
