(ns offcourse.views.base
  (:require [plumbing.core :refer-macros [fnk]]
            [services.logger :as logger]
            [offcourse.views.containers.app :refer [app]]
            [offcourse.views.components.logo :refer [logo]]
            [offcourse.views.components.actions-panel :refer [actions-panel]]
            [offcourse.views.containers.menubar :refer [menubar]]
            [shared.protocols.validatable :as va]))

(def graph
  {:container      (fnk [] app)
   :viewmodel      (fnk [[:appstate viewmodel]] viewmodel)
   :viewmodel-name (fnk [viewmodel] (va/resolve-type viewmodel))
   :user-name      (fnk [] nil)
   :base-actions   (fnk [] [:sign-in :sign-out])
   :actions        (fnk [base-actions] (into #{} base-actions))
   :respond        (fnk [responder actions]
                        (fn [[action-type :as action]]
                          (if (contains? actions action-type)
                            (responder [:requested action])
                            (logger/log :invalid-action action-type))))
   :logo           (fnk [[:appstate site-title]
                         actions
                         url-helpers]
                        (logo {:site-title site-title}
                              (select-keys actions [])
                              (select-keys url-helpers [:home-url])))
   :actions-panel  (fnk [user-name
                         respond
                         url-helpers]
                        (actions-panel {:user-name user-name}
                                       respond
                                       (select-keys url-helpers [:profile-url])))
   :menubar        (fnk [logo
                         actions-panel
                         actions
                         viewmodel-name]
                        (menubar logo actions-panel))})
