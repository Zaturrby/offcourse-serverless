(ns shared.specs.base
  (:require [cljs.spec :as spec]))

(defn user-name-length [str]
  (>= (count str) 3))

(spec/def ::user-name (spec/and string? #(user-name-length %)))
(spec/def ::flags (spec/* string?))
(spec/def ::tags #{:all})
(spec/def ::urls #{:all})
(spec/def ::url string?)
(spec/def ::user-profile nil?)
(spec/def ::collection-name string?)
(spec/def ::collection-type string?)
(spec/def ::course-slug string?)
(spec/def ::curator ::user-name)
(spec/def ::checkpoint-slug string?)
(spec/def ::checkpoint-slug string?)
(spec/def ::auth-token string?)
(spec/def ::site-title string?)
(spec/def ::user (spec/nilable (spec/keys :req-un [::user-name])))
