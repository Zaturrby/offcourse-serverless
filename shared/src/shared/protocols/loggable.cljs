(ns shared.protocols.loggable)

(defprotocol Loggable
  (-log [data] [data args-array])
  (-error [error])
  (-pipe [data] [msg data]))

(defn log
  ([data] (-log data))
  ([data & args] (-log data (to-array args))))

(defn error
  ([data explanation]
   (-error {:data data
            :explanation explanation})))

(defn pipe
  ([data] (do
            (log data)
            data))
  ([msg data] (do
                (log msg data)
                data)))
