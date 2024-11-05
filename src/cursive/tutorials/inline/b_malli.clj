(ns cursive.tutorials.inline.b-malli
  (:refer-clojure :exclude [format])
  (:require [cursive.inline.malli :refer [format]]
            [malli.core :as m]
            [malli.dev.pretty :as p]
            [malli.dev.virhe :as v]))

; Now let's see how to put all this together into a real example.

; Again, start by loading this file into the REPL, then you can evaluate
; the forms in the (comment) blocks.

; If you've used Malli, you know that when you get an exception back from it
; it can contain a lot of information which can be hard to decipher. Malli has
; some nice functions to make the display of these errors nicer. Let's see what
; those errors look like. Here are a couple of functions to print those out:

(def printer (p/-printer))

(defn pretty [ex]
  (-> ex
      (v/exception-document printer)
      (v/-print-doc printer)))


; Here are some example functions from the Malli doc, used for creating interesting
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


(comment

  ; Now you can show these errors by evaluating these forms. They'll be printed
  ; to stdout, but you'll see them inline in the editor here:

  (try
    (m/coerce :string (meta #'defn))
    (catch Exception ex
      (pretty ex)))

  (try
    (pow "2")
    (catch Exception ex
      (pretty ex)))

  (try
    (pow 4)
    (catch Exception ex
      (pretty ex)))

  (try
    (pow 4 2)
    (catch Exception ex
      (pretty ex)))

  (try
    (arg<ret 0)
    (catch Exception ex
      (pretty ex)))

  (try
    (def arg<ret
      (m/-instrument
        {:schema [:=> [:cat [:vector]] [:int {:max 6}]]}
        (fn ->plus-err [] plus-err)))
    (catch Exception ex
      (pretty ex))))


; It would be nice to have these errors in the editor. cursive.inline.malli contains
; some functions which replicate how Malli formats those errors, but instead of creating
; a fipp document for pretty printing, we'll create tree nodes for our inline viewer instead.

(comment
  ; Ctrl/Cmd-click below to see these functions:
  cursive.inline.malli

  ; Using those functions, you can get nice errors right in the editor, all expandable
  ; and explorable.

  ; Notice in this first example how the value (which comes from (meta #'defn)) is
  ; expandable. This is really nice when your data structures are big. It's a good
  ; example of how useful it is to be able to interleave custom nodes with actual data.

  (try
    (m/coerce :string (meta #'defn))
    (catch Exception ex
      (format ex)))

  (try
    (pow "2")
    (catch Exception ex
      (format ex)))

  (try
    (pow 4)
    (catch Exception ex
      (format ex)))

  (try
    (pow 4 2)
    (catch Exception ex
      (format ex)))

  (try
    (arg<ret 0)
    (catch Exception ex
      (format ex)))

  (try
    (def arg<ret
      (m/-instrument
        {:schema [:=> [:cat [:vector]] [:int {:max 6}]]}
        (fn ->plus-err [] plus-err)))
    (catch Exception ex
      (format ex)))

  ; You can use this easily by adding loading the contents of cursive.inline.malli
  ; in your application, and then creating a REPL command
  ; (https://cursive-ide.com/userguide/repl.html#repl-commands) to invoke
  ; (cursive.inline.malli/format *e). Then with a keystroke you can get these views
  ; in your editor for the last error that occurred, wherever you happen to be.

  ; These error views don't contain the actual exception, which means that you don't
  ; get a navigable stack trace. But if you want that, then just include the exception
  ; in the data you return, and Cursive will display it nicely. You could even just do
  ; something simple like this:

  (try
    (m/coerce :string (meta #'defn))
    (catch Exception ex
      {:explanation (format ex)
       :exception   ex}))

  ; It's worth emphasising - Cursive knows nothing about Malli, or even that you're
  ; using it. It's just displaying the data the REPL is returning to it, and you
  ; control what that representation looks like in the editor.

  ; Now open cursive.tutorials.inline.c-scope-capture to see another more complex example.

  ; Ctrl/Cmd-click this to go there:
  cursive.tutorials.inline.c-scope-capture




  ; keep the closing paren down here
  #_nil)
