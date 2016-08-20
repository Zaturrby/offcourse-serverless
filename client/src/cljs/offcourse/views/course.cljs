(ns offcourse.views.course
  (:require [offcourse.protocols.decoratable :as dc]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.queryable :as qa]))

(def graph
  {:course-data   (fnk [appstate] (-> appstate :viewmodel :course))
   :course        (fnk [appstate course-data user-name]
                       (if-let [course (-> appstate
                                           (qa/search course-data))]
                         (dc/decorate course user-name nil)
                         nil))
   :checkpoints   (fnk [appstate course]
                       (map #(dc/decorate % appstate) (:checkpoints course)))
   :main          (fnk [course checkpoints [:components sheets]
                        [:url-helpers collection-url checkpoint-url]
                        handlers]
                       (let [url-helpers {:checkpoint-url (partial checkpoint-url
                                                                   (:curator course)
                                                                   (:course-slug course))
                                          :collection-url collection-url}]
                         (sheets checkpoints url-helpers handlers (:trackable? (meta course)))))
   :dashboard     (fnk [url-helpers course handlers [:components card dashboard]]
                       (dashboard {:main (when course (card course url-helpers handlers))}))})
