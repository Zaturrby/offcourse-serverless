(set-env!
 :source-paths    #{"src/js" "src/cljs" "src/cljc"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs              "1.7.228-1"      :scope "test"]
                 [adzerk/boot-cljs-repl         "0.3.2"          :scope "test"]
                 [adzerk/boot-reload            "0.4.10"         :scope "test"]
                 [ring/ring-devel               "1.3.2"          :scope "test"]
                 [com.cemerick/piggieback       "0.2.1"          :scope "test"]
                 [org.clojure/tools.nrepl       "0.2.12"         :scope "test"]
                 [pandeiro/boot-http            "0.7.1-SNAPSHOT" :scope "test"]
                 [weasel                        "0.7.0"          :scope "test"]
                 [hashobject/boot-s3            "0.1.2-SNAPSHOT" :scope "test"]
                 [crisptrutski/boot-cljs-test   "0.2.2-SNAPSHOT" :scope "test"]
                 [boot-environ                  "1.0.2"          :scope "test"]
                 [boot-codox "0.9.5" :scope "test"]
                 [environ                       "1.0.2"]
                 [org.martinklepsch/boot-garden "1.3.0-0"]
                 [metosin/ring-http-response  "0.6.5"]
                 [com.stuartsierra/component  "0.3.1"]
                 [org.clojure/clojurescript   "1.9.89"]
                 [org.clojure/core.async      "0.2.374"]
                 [org.clojure/core.match      "0.3.0-alpha4"]
                 [prismatic/schema            "1.0.4"]
                 [prismatic/plumbing          "0.5.2"]
                 [com.rpl/specter             "0.9.2"]
                 [danlentz/clj-uuid           "0.1.6"]
                 [markdown-clj                "0.9.77"]
                 [cljs-ajax "0.5.3"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [medley                      "0.7.0"]
                 [env/faker                   "0.4.0"]
                 [compojure                   "1.4.0"]
                 [hiccup                      "1.0.5"]
                 [rum                         "0.6.0"]
                 [sablono                     "0.5.3"]
                 [bidi                        "1.25.0"]
                 [funcool/cuerdas             "0.7.0"]
                 [kibu/pushy                  "0.3.6"]
                 [cljsjs/auth0                "6.3.0-0"]
                 [cljsjs/auth0-lock           "9.2.1-0"]
                 [cljsjs/react                "0.14.3-0"]
                 [cljsjs/react-dom            "0.14.3-1"]
                 [cljsjs/react-dom-server     "0.14.3-0"]])


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
  (set-env! :source-paths #(conj % "src/clj"))
  (task-options! garden {:styles-var   'offcourse.styles.index/base
                         :vendors ["webkit" "moz"]
                         :auto-prefix #{:user-select :column-count :column-gap}
                         :output-to    "css/main.css"
                         :pretty-print true})
  (garden))

(deftask dev []
  (set-env! :source-paths #(conj % "src-dev/cljs" "src-dev/clj"))
  (task-options! target {:dir #{"dev/"}})
  (comp (serve :handler 'history-handler/app)
        (watch)
        (speak)
        (reload :on-jsload 'offcourse.core/reload)
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
