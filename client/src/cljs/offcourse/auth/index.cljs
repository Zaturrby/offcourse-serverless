(ns offcourse.auth.index
  (:require cljsjs.auth0-lock
            [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.auth.authenticate :as ac]
            [offcourse.auth.get :as get-impl]
            [offcourse.protocols.queryable :as qa :refer [Queryable]]
            [shared.protocols.responsive :as ri :refer [Responsive]]))

(defn init [{:keys [config] :as auth}]
  (assoc auth :provider (js/Auth0Lock. (:clientID config) (:domain config))))

(defrecord Auth [component-name reactions]
  Queryable
  (-get [auth query] (get-impl/get auth query))
  Lifecycle
  (start [auth]
    (let [auth-token (qa/get auth {:type :auth-token})]
      (when auth-token
        (ri/respond auth [:granted {:auth-token auth-token}]))
      (-> auth
          init
          ri/listen)))
  (stop [auth] (ri/mute auth))
  Responsive
  (-respond [auth event] (ri/respond auth event))
  (-react [auth event]
    #_(ac/sign-in ac/signout)
    auth)
  (-mute [auth] (ri/mute auth))
  (-listen [auth] (ri/listen auth)))

(defn create [component-name reactions] (->Auth component-name reactions))
