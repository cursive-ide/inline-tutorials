(ns cursive.tutorials.inline.auxiliary.malli
  (:require [malli.core :as m]
            [malli.dev.pretty :as p]
            [malli.dev.virhe :as v]))


(def printer (p/-printer))

(defn pretty
  "Print a Malli exception to stdout using Malli's pretty printer."
  [ex]
  (-> ex
      (v/exception-document printer)
      (v/-print-doc printer)))


; Some example functions from the Malli doc, used for creating interesting
; errors to demo.

(def pow
  (m/-instrument
    {:schema [:=> [:cat :int] [:int {:max 6}]]}
    (fn [x] (* x x))))

(def arg<ret
  (m/-instrument
    {:schema [:=>
              [:cat :int]
              :int
              [:fn {:error/message "argument should be less than return"}
               (fn [[[arg] ret]] (< arg ret))]]}
    (fn [x] x)))

(defn plus-err
  [x] (inc x))
