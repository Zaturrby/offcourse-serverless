(def project 'ofcourse/shared)
(def version "0.1.0-SNAPSHOT")

(set-env!
 :resource-paths #{"src"}
 :source-paths #{"src"}
 :dependencies  '[[adzerk/boot-cljs            "1.7.228-1"      :scope "test"]
                  [adzerk/boot-cljs-repl       "0.3.3"          :scope "test"]
                  [adzerk/boot-reload          "0.4.12"         :scope "test"]
                  [pandeiro/boot-http          "0.7.3"          :scope "test"]
                  [crisptrutski/boot-cljs-test "0.3.0-SNAPSHOT" :scope "test"]
                  [boot-codox                  "0.9.6"          :scope "test"]
                  [com.cemerick/piggieback     "0.2.2-SNAPSHOT" :scope "test"]
                  [weasel                      "0.7.0"          :scope "test"]
                  [org.clojure/tools.nrepl     "0.2.12"         :scope "test"]
                  [com.rpl/specter               "0.12.0"]
                  [org.clojure/clojure         "1.9.0-alpha10"]
                  [org.clojure/core.async      "0.2.385"]
                  [org.clojure/test.check      "0.9.0"]
                  [org.clojure/clojurescript   "1.9.216"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[crisptrutski.boot-cljs-test  :refer [exit! test-cljs]]
 '[codox.boot :refer [codox]]
 '[pandeiro.boot-http    :refer [serve]])

(task-options! pom {:project     'offcourse/shared
                    :version     version
                    :description "HELLO WORLD"
                    :url         "http://example/FIXME"
                    :scm         {:url "https://github.com/yourname/specs"}
                    :license     {"Eclipse Public License"
                                  "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build
  "Build and install the project locally."
  []
  (comp (pom)
        (jar)
        (install)
        (codox :language :clojurescript
               :name "offcourse-shared")
        (target)))

(deftask dev []
  (comp (watch)
        (cljs-repl)
        (build)))
