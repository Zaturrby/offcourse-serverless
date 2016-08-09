(set-env!
 :resource-paths #{"src" "../protocols/src" "../specs/src" "../services/src" "../models/src"}
 :checkouts     '[[offcourse/specs            "0.1.0-SNAPSHOT"]
                  [offcourse/models            "0.1.0-SNAPSHOT"]
                  [offcourse/protocols            "0.1.0-SNAPSHOT"]
                  [offcourse/services          "0.1.0-SNAPSHOT"]]
 :dependencies '[[adzerk/boot-cljs            "1.7.228-1"      :scope "test"]
                 [adzerk/boot-cljs-repl       "0.3.3"          :scope "test"]
                 [adzerk/boot-reload          "0.4.12"          :scope "test"]
                 [pandeiro/boot-http          "0.7.3" :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.0-SNAPSHOT" :scope "test"]
                 [org.clojure/clojure         "1.9.0-alpha10"]
                 [org.clojure/core.async      "0.2.385"]
                 [org.clojure/test.check "0.9.0"]
                 [org.clojure/clojurescript   "1.9.89"]
                 [com.cemerick/piggieback     "0.2.2-SNAPSHOT"          :scope "test"]
                 [offcourse/specs            "0.1.0-SNAPSHOT"]
                 [offcourse/services          "0.1.0-SNAPSHOT"]
                 [offcourse/models            "0.1.0-SNAPSHOT"]
                 [offcourse/protocols            "0.1.0-SNAPSHOT"]
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
