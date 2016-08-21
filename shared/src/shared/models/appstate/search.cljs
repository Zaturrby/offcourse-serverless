(ns shared.models.appstate.search
  (:require [com.rpl.specter :refer [ALL select-first]]
            [medley.core :as medley]
            [shared.models.appstate.paths :as paths]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [shared.protocols.validatable :as va]
            [services.logger :as logger]))


(defmulti search (fn [_ query] (va/resolve-type query)))

(defmethod search :collection [{:keys [collections] :as ds} {:keys [collection]}]
  (when collections
    (let [{:keys [collection-type collection-name]} collection]
      (select-first (paths/collection collection-type collection-name) ds))))

(defmethod search :courses [{:keys [courses] :as ds} query]
  (when courses
    (let [course-ids (map :course-id (:courses query))]
      (when-let [courses (keep #(select-first (paths/course %) ds) course-ids)]
        (if (empty? courses) nil courses)))))

(defmethod search :course [{:keys [courses] :as ds} course]
  (when courses
    (select-first (paths/course course) ds)))

(defmethod search :checkpoint [{:keys [courses] :as ds} {:keys [checkpoint]}]
  (when courses
    (select-first (paths/checkpoint checkpoint) ds)))

(defmethod search :resources [{:keys [resources] :as ds} query]
  (when resources
    (let [urls (map :url (:resources query))]
      (when-let [resources (keep #(select-first (paths/resource %) ds) urls)]
        (if (empty? resources) nil resources)))))

(defmethod search :resource [{:keys [resources] :as ds} {:keys [resource]}]
  (when resources
    (when-let [{:keys [url]} resource]
      (select-first (paths/resource url) ds))))
