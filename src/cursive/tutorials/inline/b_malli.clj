(ns cursive.tutorials.inline.b-malli
  (:refer-clojure :exclude [format])
  (:require [cursive.inline.malli :refer [format]]
            [cursive.inline.nodes :refer :all]
            [cursive.tutorials.inline.auxiliary.malli :refer :all]
            [malli.core :as m]))

; Now let's see how to put all this together into a real example.

; Start by loading this file into the REPL, then you can evaluate
; the forms in the (comment) blocks.

(comment
  ; Malli has some nice functions to make the display of its errors better.
  ; Let's see some examples of Malli errors. These are printed on stdout.

  (try
    ; Coercion error
    (m/coerce :string (meta #'defn))
    (catch Exception ex
      (pretty ex)))

  (try
    ; Function argument error
    (pow "2")
    (catch Exception ex
      (pretty ex)))

  (try
    ; Function return error
    (pow 4)
    (catch Exception ex
      (pretty ex)))

  (try
    ; Function arity error
    (pow 4 2)
    (catch Exception ex
      (pretty ex)))

  (try
    ; Function guard error
    (arg<ret 0)
    (catch Exception ex
      (pretty ex)))

  (try
    ; Invalid schema error
    (def invalid
      (m/-instrument
        {:schema [:=> [:cat [:vector]] [:int {:max 6}]]}
        (fn ->plus-err [] plus-err)))
    (catch Exception ex
      (pretty ex))))


; It would be nice to have these errors in the editor. cursive.inline.malli contains
; some functions which replicate how Malli formats those errors, but instead of printing,
; we'll create tree nodes instead.

(comment
  ; Navigate below to see these functions:
  cursive.inline.malli

  ; Using those functions, you can get nice errors right in the editor, all expandable
  ; and explorable.

  (try
    ; Notice that the erroneous value appears in the tree as data
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
    (def invalid
      (m/-instrument
        {:schema [:=> [:cat [:vector]] [:int {:max 6}]]}
        (fn ->plus-err [] plus-err)))
    (catch Exception ex
      (format ex)))

  (title-node "How can I use this?"
    (node {:presentation [{:text  "; You can use this easily by loading the contents of "
                           :color :comment}
                          {:form 'cursive.inline.malli}]})
    (node {:presentation [{:text  "; in your application, and then creating a "
                           :color :comment}
                          {:text  "REPL command"
                           :color :link}
                          {:text  " to invoke"
                           :color :comment}]
           :action       :browse
           :url          "https://cursive-ide.com/userguide/repl.html#repl-commands"})
    (node {:presentation [{:text  "; "
                           :color :comment}
                          {:form '(cursive.inline.malli/format *e)}
                          {:text  ". Then with a keystroke you can get these views"
                           :color :comment}]})
    (comment-node "; in your editor for the last error that occurred, wherever you happen to be."))

  (title-node "Anything else?"
    (comment-node "; These error views don't contain the actual exception, which means that you don't")
    (comment-node "; get a navigable stack trace. But if you want that, then just include the exception")
    (comment-node "; in the data you return, and Cursive will display it nicely. You could even just do")
    (comment-node "; something simple like this:")
    (try
      (m/coerce :string (meta #'defn))
      (catch Exception ex
        {:explanation (format ex)
         :exception   ex})))



  (title-node "You are done with this tutorial"
    (comment-node "; Remember - Cursive knows nothing about Malli.")
    (comment-node "; It's just displaying the data the REPL is returning to it.")
    (comment-node "; Now go to the next step to see another more complex example.")
    (node {:presentation [{:text  "cursive.tutorials.inline.c-scope-capture"
                           :color :link}]
           :action       :navigate
           :file         "cursive/tutorials/inline/c_scope_capture.clj"
           :line         7
           :column       3})))

