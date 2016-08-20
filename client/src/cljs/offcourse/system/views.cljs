(ns offcourse.system.views
  (:require [offcourse.views.base :as bv]
            [offcourse.views.checkpoint :as cpv]
            [offcourse.views.signup :as su]
            [offcourse.views.course :as cov]
            [offcourse.views.collection :as clv]))

(def views
  {:loading    bv/graph
   :signup     (merge bv/graph su/graph)
   :course     (merge bv/graph cov/graph)
   :collection (merge bv/graph clv/graph)
   :checkpoint (merge bv/graph cpv/graph)})
