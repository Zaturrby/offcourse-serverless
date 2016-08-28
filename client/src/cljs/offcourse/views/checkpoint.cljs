(ns offcourse.views.checkpoint
  (:require [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]
            [shared.protocols.queryable :as qa]))

(def graph
  {:checkpoint-data (fnk [viewmodel] (or (-> viewmodel :checkpoint) {:checkpoint-slug nil}))
   :course-data     (fnk [viewmodel] (-> viewmodel :course))
   :course          (fnk [appstate
                          course-data
                          checkpoint-data
                          user-name]
                         (some-> appstate
                                 (qa/get course-data)
                                 (dc/decorate user-name checkpoint-data)))
   :checkpoint      (fnk [appstate
                          course
                          checkpoint-data]
                         (some-> course
                                 (qa/get checkpoint-data)
                                 (dc/decorate appstate)))
   :actions    (fnk [base-actions handlers]
                    (->> handlers
                         (select-keys [:toggle-checkpoint])
                         (merge base-actions)))
   :main            (fnk [checkpoint
                          [:components viewer]]
                         (viewer checkpoint nil nil))
   :dashboard       (fnk [url-helpers
                          user-name
                          course
                          actions
                          [:components card dashboard]]
                         (when course
                           (dashboard {:main (card course actions url-helpers)})))})
