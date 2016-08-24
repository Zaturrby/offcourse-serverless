(ns shared.protocols.renderable)

(defprotocol Renderable
  (-render [this payload] [this views container-element]))

(defn render
  ([this payload](-render this payload))
  ([this views container-element] (-render this views container-element)))
