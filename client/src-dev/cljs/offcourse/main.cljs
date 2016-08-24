(ns offcourse.main
  (:require [com.stuartsierra.component :as component]
            [offcourse.adapters.aws.index :as aws]
            [offcourse.core :as core]
            [shared.models.appstate.index :as model]))

(defonce app (atom nil))

(defonce appstate (atom (model/create {:site-title "Offcourse_"})))

(defonce auth-config {:domain "yeehaa.eu.auth0.com"
                  :clientID "Z1J0CyMzZfIbOfBSVaMWJakoIrxm4Tfs"})
(def adapters
  {:query [{:adapter           aws/new-db
            :name              :aws
            :resources         #{:user-profile :course :collection}
            :endpoint          "https://70zxd74j8l.execute-api.eu-central-1.amazonaws.com/yeehaa/query-endpoint"}]})

(defn init []
  (do
    (enable-console-print!)
    (reset! app (core/app appstate adapters auth-config))
    (reset! app (component/start @app))))

(defn reload []
  (do
    (enable-console-print!)
    (reset! app (core/app appstate adapters auth-config))
    (reset! app (component/start @app))))

(defn stop []
  (component/stop @app))
