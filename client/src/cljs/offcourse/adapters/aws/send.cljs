(ns offcourse.adapters.aws.send
  (:require [ajax.core :refer [POST GET]]
            [cljs.core.async :refer [chan]]
            [services.logger :as logger]
            [shared.models.event.index :as event]
            [shared.protocols.validatable :as va]
            [clojure.walk :as walk])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [name res]
  (let [{:keys [type payload]} (js->clj (.parse js/JSON res) :keywordize-keys true)]
    (event/create [name (keyword type) payload])))

(defn yaml-file? [path]
  (re-find #"\.yaml?" path))

(def base-name "https://api.github.com")
(def repo-name "sample-content")
(def org-name "Offcourse")
(def sha "04871f3539e3ac197a04f5904823eff705691521")
(def tree-url (str base-name "/repos/" org-name "/" repo-name "/git/trees/" sha))
(defn content-url [file-path] (str base-name "/repos/" org-name "/" repo-name "/contents/" file-path))

(defn handle-content-response [res]
  (let [content (->> res
                     walk/keywordize-keys
                     :content
                     (.atob js/window))]
    (logger/log :content content)))

(defn handle-github-response [res]
  (let [paths (->> res
                   walk/keywordize-keys
                   :tree
                   (map :path)
                   (filter yaml-file?))
        path (last paths)
        content-url (content-url path)]
    (GET content-url
        {:format :json
         :handler handle-content-response})))

(defn send [{:keys [name endpoint]} [_ query :as event]]
  (let [c (chan)
        auth-token ""]
    (GET tree-url
        {:format :json
         :handler handle-github-response})
    (POST endpoint
        {:headers {:Authorization (str "Bearer " auth-token)}
         :params (clj->js {:type :request-data
                           :payload query})
         :format :json
         :handler #(go (>! c (handle-response name %)))})
    c))
