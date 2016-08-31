(ns shared.models.appstate.paths
  (:require [com.rpl.specter :refer [ALL]]
            [cuerdas.core :as str]))


(defn course [{:keys [course-id course-slug curator goal] :as course}]
  [:courses ALL #(and (= (-> % :goal str/slugify) course-slug)
                      (= (:curator %) curator))])

(defn checkpoint [{:keys [course-id checkpoint-id checkpoint-slug]}]
  [(course course-id) :checkpoints ALL #(= (-> % :task str/slugify) checkpoint-slug)])

(defn resource [url]
  [:resources ALL #(= (:url %) url)])
