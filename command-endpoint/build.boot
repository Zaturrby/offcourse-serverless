(set-env!
 :resource-paths #{"src" "html"}
 :dependencies '[[adzerk/boot-cljs            "1.7.228-1"      :scope "test"]
                 [adzerk/boot-cljs-repl       "0.3.3"          :scope "test"]
                 [adzerk/boot-reload          "0.4.12"          :scope "test"]
                 [pandeiro/boot-http          "0.7.3" :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.0-SNAPSHOT" :scope "test"]
                 [org.clojure/clojure         "1.9.0-alpha10"]
                 [org.clojure/core.async      "0.2.374"]
                 [org.clojure/clojurescript   "1.9.89"]
                 [com.cemerick/piggieback     "0.2.2-SNAPSHOT"          :scope "test"]
                 [weasel                      "0.7.0"          :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.12"         :scope "test"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[crisptrutski.boot-cljs-test  :refer [exit! test-cljs]]
 '[pandeiro.boot-http    :refer [serve]])

(deftask build []
  (task-options! cljs   {:compiler-options {:optimizations :simple
                                            :target :nodejs}})
  (comp (cljs)
        (target)))

(deftask dev []
  (comp (watch)
        (speak)
        (cljs-repl)
        (build)))