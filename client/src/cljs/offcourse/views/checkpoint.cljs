(ns offcourse.views.checkpoint
  (:require [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]
            [offcourse.views.containers.dashboard :refer [dashboard]]
            [offcourse.views.components.viewer :refer [viewer]]
            [offcourse.views.components.card :refer [card]]
            [shared.protocols.queryable :as qa]))

(def graph
  {:checkpoint-data (fnk [viewmodel] (or (-> viewmodel :checkpoint) {:checkpoint-slug nil}))
   :course-data     (fnk [viewmodel] (-> viewmodel :course))
   :course          (fnk [appstate
                          course-data
                          checkpoint-data
                          routes
                          user-name]
                         (some-> appstate
                                 (qa/get course-data)
                                 (dc/decorate user-name checkpoint-data routes)))
   :checkpoint      (fnk [appstate
                          course
                          checkpoint-data]
                         (some-> course
                                 (qa/get checkpoint-data)
                                 (dc/decorate appstate)))
   :actions    (fnk [base-actions]
                    (->> base-actions
                         (into #{})))
   :main            (fnk [checkpoint]
                         (viewer checkpoint nil nil))
   :dashboard       (fnk [url-helpers
                          user-name
                          course
                          actions]
                         (when course
                           (dashboard {:main (card course actions url-helpers)})))})
