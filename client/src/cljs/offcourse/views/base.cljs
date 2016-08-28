(ns offcourse.views.base
  (:require [plumbing.core :refer-macros [fnk]]))

(def graph
  {:container     (fnk [[:components app]] app)
   :viewmodel     (fnk [[:appstate viewmodel]] viewmodel)
   :user-name     (fnk [] nil)
   :base-actions  (fnk [handlers] (select-keys handlers [:sign-in :sign-out]))
   :actions       (fnk [base-actions] base-actions)
   :logo          (fnk [[:appstate site-title]
                        actions
                        url-helpers
                        [:components logo]]
                       (logo {:site-title site-title}
                             (select-keys actions [])
                             (select-keys url-helpers [:home-url])))
   :actions-panel  (fnk [user-name
                         actions
                         url-helpers
                         [:components actions-panel]]
                        (actions-panel {:user-name user-name}
                                       (select-keys actions [:sign-in :sign-out])
                                       (select-keys url-helpers [:profile-url])))
   :menubar       (fnk [logo
                        actions-panel
                        [:components menubar]]
                       (menubar logo actions-panel))})
