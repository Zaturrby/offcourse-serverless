(ns shared.models.viewmodel.to-url
  (:require [bidi.bidi :as bidi]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [services.logger :as logger]))

(defmulti to-url (fn [vm]
                   (first (spec/conform ::specs/viewmodel vm))))

(defmethod to-url :signup-view [{:keys [type dependencies] :as vm} routes]
  (bidi/path-for routes :new-user-view))

(defmethod to-url :checkpoint-view [{:keys [type course checkpoint] :as vm} routes]
  (let [{:keys [course-slug organization curator]} course
        {:keys [checkpoint-slug]}                  checkpoint]
    (cond
      (and organization checkpoint-slug) (bidi/path-for routes
                                                        :checkpoint-org-view
                                                        :curator curator
                                                        :course-slug course-slug
                                                        :checkpoint-slug checkpoint-slug
                                                        :organization organization)
      (and organization course-slug) (bidi/path-for routes
                                                    :course-org-view
                                                    :curator curator
                                                    :course-slug course-slug
                                                    :organization organization)
      checkpoint-slug (bidi/path-for routes
                                     :checkpoint-view
                                     :curator curator
                                     :course-slug course-slug
                                     :checkpoint-slug checkpoint-slug)
      course-slug     (bidi/path-for routes
                                     :course-view
                                     :curator curator
                                     :course-slug course-slug))))

(defmethod to-url :collection-view [{:keys [type collection] :as vm} routes]
  (let [{:keys [collection-type collection-name]} collection]
    (bidi/path-for routes :collection-view :collection-type collection-type :collection-name collection-name)))
