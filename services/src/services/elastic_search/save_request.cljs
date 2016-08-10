(ns services.elastic-search.save-request
  (:require [cljs.core.async :as async :refer [chan]]
            [cljs.nodejs :as node]))

(def AWS (node/require "aws-sdk"))
(def path (node/require "path"))

(def endpoint-url (.. js/process -env -ELASTICSEARCH_ENDPOINT))
(def endpoint (when endpoint-url (AWS.Endpoint. endpoint-url)))

(def region (.. js/process -env -SERVERLESS_REGION))
(def creds (AWS.EnvironmentCredentials. "AWS"))
(def HTTP (AWS.NodeHttpClient.))

(defn create-request [{:keys [course-id] :as item} index-name]
  (let [req (AWS.HttpRequest. endpoint)
        headers (aget req "headers")]
    (aset req "method" "POST")
    (aset req "path"  (.join path "/" "offcourse" index-name course-id))
    (aset req "region" region)
    (aset headers "presigned-expires" false)
    (aset headers "Host" (aget endpoint "host"))
    (aset req "body" (.stringify js/JSON (clj->js item)))
    req))

(defn sign-request [req]
  (let [signer (AWS.Signers.V4. req "es")]
    (.addAuthorization signer creds (js/Date.))
    req))

(defn handle-response [resp c]
  (let [item (atom "")]
    (.on resp "data" #(swap! item str %1))
    (.on resp "end" #(do
                       (async/put! c @item)
                       (async/close! c)))))
(defn -send [req]
  (let [c (chan)]
    (.handleRequest HTTP req nil #(handle-response %1 c))
    c))

(defn send [index-name item]
  (-> item
      (create-request index-name)
      sign-request
      -send))
