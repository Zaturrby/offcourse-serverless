(def project 'offcourse/styles)
(def version "0.1.0-SNAPSHOT")

(set-env!
 :source-paths    #{"src/"})
 ; :dependencies '[[org.martinklepsch/boot-garden "1.3.2-0"]
 ;                 [prismatic/plumbing          "0.5.3"]])

; (require '[org.martinklepsch.boot-garden :refer [garden]])

(deftask build
  "Build and install the project locally."
  []
  (task-options!
   pom {:project     'offcourse/styles
        :version     version
        :description "Offcourse shared stylesheets"
        :url         "http://example/FIXME"
        :scm         {:url "https://github.com/yourname/styles"}
        :license     {"Eclipse Public License"
                      "http://www.eclipse.org/legal/epl-v10.html"}})
  (comp (pom)
        (jar)
        (install)))

(deftask dev []
  (comp (watch)
        (build)))