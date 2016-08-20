(ns offcourse.auth.index
  (:require cljsjs.auth0-lock
            [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.auth.authenticable :as ac-impl]
            [offcourse.auth.get :as get-impl]
            [offcourse.protocols.authenticable :as ac :refer [Authenticable]]
            [offcourse.protocols.queryable :as qa :refer [Queryable]]
            [offcourse.protocols.responsive :as ri :refer [Responsive]]))

(defn init [{:keys [config] :as auth}]
  (assoc auth :provider (js/Auth0Lock. (:clientID config) (:domain config))))

(defrecord Auth []
  Queryable
  (-get [auth query] (get-impl/get auth query))
  Lifecycle
  (start [auth]
    (let [auth-token (qa/get auth {:type :auth-token})]
      (when auth-token
        (ri/respond auth [:found {:auth-token auth-token}]))
      (-> auth
          init
          ri/listen)))
  (stop [auth] (ri/mute auth))
  Authenticable
  (-sign-in [auth] (ac-impl/sign-in auth))
  (-sign-out [auth] (ac-impl/sign-out auth))
  Responsive
  (-respond [auth event] (ri/respond auth event))
  (-mute [auth] (ri/mute auth))
  (-listen [auth] (ri/listen auth)))

(defn new [] (map->Auth {:component-name :auth
                         :reactions {:requested-sign-in  ac/sign-in
                                     :requested-sign-out ac/sign-out}}))
