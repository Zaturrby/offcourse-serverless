(ns offcourse.system.routes
  (:require [bidi.bidi :refer [path-for]]
            [shared.models.viewmodel.index :as viewmodel]
            [shared.models.checkpoint.index :as cp]
            [shared.models.course.index :as co]))

(def home-route        [:collection-name])
(def curator-routes    [:curator])
(def new-user-route    "signup")
(def course-routes     (conj curator-routes "/courses/" :course-slug))
(def collection-routes [:collection-type "/" :collection-name])
(def checkpoint-routes (conj course-routes "/checkpoints/" :checkpoint-slug))

(def table ["/" {home-route        :home-view
                 checkpoint-routes :checkpoint-view
                 new-user-route    :new-user-view
                 course-routes     :course-view
                 collection-routes :collection-view
                 true              :home-view}])

(def query-constructors
  {:home-view       (fn []     (viewmodel/create :home-view))
   :collection-view (fn [data] (viewmodel/create :collection-view data))
   :course-view     (fn [data] (viewmodel/create :course-view data))
   :checkpoint-view (fn [data] (viewmodel/create :checkpoint-view data))})
