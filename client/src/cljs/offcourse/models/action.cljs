(ns offcourse.models.action)

(defrecord Action [type source payload])

(defn new [{:keys [component-name] :as source} type payload]
  (->Action type component-name payload))
