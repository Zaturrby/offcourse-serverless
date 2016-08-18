(comment "check cuerdas and specter for api updates")

(set-env!
 :source-paths    #{"src/js" "src/clj" "src/cljs" "src/cljc" "../protocols/src"
                    "../specs/src" "../services/src" "../models/src" "../styles/src"}
 :resource-paths  #{"resources"}
 :checkouts     '[[offcourse/specs             "0.1.0-SNAPSHOT"]
                  [offcourse/models            "0.1.0-SNAPSHOT"]
                  [offcourse/styles            "0.1.0-SNAPSHOT"]
                  [offcourse/protocols         "0.1.0-SNAPSHOT"]
                  [offcourse/services          "0.1.0-SNAPSHOT"]]
 :dependencies '[[adzerk/boot-cljs              "1.7.228-1"      :scope "test"]
                 [adzerk/boot-cljs-repl         "0.3.3"          :scope "test"]
                 [adzerk/boot-reload            "0.4.12"         :scope "test"]
                 [ring/ring-devel               "1.6.0-beta4"          :scope "test"]
                 [com.cemerick/piggieback       "0.2.2-SNAPSHOT"          :scope "test"]
                 [org.clojure/tools.nrepl       "0.2.12"         :scope "test"]
                 [pandeiro/boot-http            "0.7.3" :scope "test"]
                 [weasel                        "0.7.0"          :scope "test"]
                 [hashobject/boot-s3            "0.1.2-SNAPSHOT" :scope "test"]
                 [crisptrutski/boot-cljs-test   "0.3.0-SNAPSHOT" :scope "test"]
                 [boot-codox "0.9.6" :scope "test"]
                 [prismatic/schema            "1.0.4"]
                 [org.martinklepsch/boot-garden "1.3.2-0"]
                 [metosin/ring-http-response  "0.8.0"]
                 [com.stuartsierra/component  "0.3.1"]
                 [org.clojure/clojurescript   "1.9.89"]
                 [org.clojure/core.async      "0.2.385"]
                 [org.clojure/core.match      "0.3.0-alpha4"]
                 [prismatic/plumbing          "0.5.3"]
                 [com.rpl/specter             "0.9.2"]
                 [markdown-clj                "0.9.89"]
                 [cljs-ajax "0.5.8"]
                 [medley                      "0.8.2"]
                 [env/faker                   "0.4.0"]
                 [compojure                   "1.6.0-beta1"]
                 [hiccup                      "1.0.5"]
                 [rum                         "0.10.5"]
                 [sablono                     "0.7.3"]
                 [bidi                        "2.0.9"]
                 [offcourse/specs             "0.1.0-SNAPSHOT"]
                 [offcourse/services          "0.1.0-SNAPSHOT"]
                 [offcourse/models            "0.1.0-SNAPSHOT"]
                 [offcourse/protocols         "0.1.0-SNAPSHOT"]
                 [offcourse/styles            "0.1.0-SNAPSHOT"]
                 [funcool/cuerdas             "0.7.0"]
                 [kibu/pushy                  "0.3.6"]
                 [cljsjs/auth0                "6.3.0-0"]
                 [cljsjs/auth0-lock           "9.2.1-0"]
                 [cljsjs/react                "15.3.0-0"]
                 [cljsjs/react-dom            "15.3.0-0"]
                 [cljsjs/react-dom-server     "15.3.0-0"]])


(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[org.martinklepsch.boot-garden :refer [garden]]
 '[crisptrutski.boot-cljs-test  :refer [exit! test-cljs]]
 '[pandeiro.boot-http    :refer [serve]])

(deftask testing []
  (merge-env! :resource-paths #{"test"})
  identity)

(deftask auto-test []
  (comp (testing)
        (watch)
        (speak)
        (test-cljs)))

(deftask css []
  (task-options! garden {:styles-var    'localstyles.index/base
                         :vendors       ["webkit" "moz"]
                         :auto-prefix   #{:user-select :column-count :column-gap}
                         :output-to     "css/main.css"
                         :pretty-print  true})
  (comp (garden)
        (target)))

(deftask dev []
  (set-env! :source-paths #(conj % "src-dev/cljs" "src-dev/clj"))
  (task-options! target {:dir #{"dev/"}})
  (comp (serve :handler 'history-handler/app)
        (watch)
        (speak)
        (reload :on-jsload 'offcourse.main/reload)
        (cljs-repl)
        (cljs :source-map true :optimizations :none)
        (css)
        (target)))

(deftask test []
  (comp (testing)
        (test-cljs)
        (exit!)))

(deftask build []
  (set-env! :source-paths #(conj % "src-prod/cljs"))
  (task-options! target {:dir #{"dist/"}})
  (comp (cljs :optimizations :advanced)
        (css)
        (target)))
