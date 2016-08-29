(ns offcourse.system.index
  (:require [com.stuartsierra.component :as component]
            [offcourse.api.index :as api]
            [offcourse.appstate.index :as appstate]
            [offcourse.auth.index :as auth]
            [offcourse.router.index :as router]
            [offcourse.system.handlers :refer [handlers]]
            [offcourse.protocol-extensions.decoratable]
            [offcourse.system.plumbing :as plumbing]
            [offcourse.system.routes :as routes]
            [offcourse.system.ui-components :refer [ui-components]]
            [offcourse.system.views :refer [views]]
            [offcourse.ui.index :as ui]
            [services.logger :as logger]))

(defn connect-to-repository [{:keys [adapter] :as config}]
  (component/start (adapter (select-keys config [:name :endpoint :resources]))))

(defn system [appstate repositories auth-config]
  (let [channels plumbing/channels]
    (component/system-map
     :repositories           (map connect-to-repository (:query repositories))
     :api-channels           (:api channels)
     :api-triggers           [:not-found]
     :api-responses          [:found :not-found :failed]
     :api                    (component/using (api/create :api)
                                              {:channels     :api-channels
                                               :triggers     :api-triggers
                                               :responses    :api-responses
                                               :repositories :repositories})
     :auth-channels           (:auth channels)
     :auth-triggers           [:requested]
     :auth-responses          [:granted :revoked]
     :auth-config             auth-config
     :auth                    (component/using (auth/create :auth)
                                               {:channels  :auth-channels
                                                :triggers  :auth-triggers
                                                :responses :auth-responses
                                                :config    :auth-config})

     :routes                 routes/table
     :query-constructors     routes/query-constructors
     :router-triggers        [:refreshed]
     :router-responses       [:requested]
     :router-channels        (:router channels)
     :router                 (component/using (router/create :router)
                                              {:channels           :router-channels
                                               :triggers           :router-triggers
                                               :responses          :router-responses
                                               :routes             :routes
                                               :query-constructors :query-constructors})
     :appstate-atom          appstate
     :appstate-triggers      [:granted :rendered :requested :found :not-found]
     :appstate-responses     [:refreshed :requested :not-found]
     :appstate-channels      (:appstate channels)
     :appstate               (component/using (appstate/create :appstate)
                                              {:channels  :appstate-channels
                                               :triggers  :appstate-triggers
                                               :responses :appstate-responses
                                               :state     :appstate-atom})

     :views                  views
     :view-components        ui-components
     :view-handlers          handlers
     :url-helpers            routes/url-helpers
     :ui-triggers            [:refreshed]
     :ui-responses           [:rendered :requested]
     :ui-channels            (:ui channels)
     :ui                     (component/using (ui/create :ui)
                                              {:channels    :ui-channels
                                               :triggers    :ui-triggers
                                               :responses   :ui-responses
                                               :url-helpers :url-helpers
                                               :handlers    :view-handlers
                                               :routes      :routes
                                               :components  :view-components
                                               :views       :views}))))
