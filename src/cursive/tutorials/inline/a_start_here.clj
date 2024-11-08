(ns cursive.tutorials.inline.a-start-here
  (:require [cursive.inline.nodes :refer :all]
            [clojure.pprint :as pp]))

; First, start the REPL and then load this file into it.

; Then evaluate each form from the comment block below to see how it behaves.

(comment
  ; Here's how the inline eval display works now. Eval this form:

  (meta #'defn)

  ; The data is displayed as a tree, where nodes in the tree with children can
  ; be navigated and expanded, via the keyboard or the mouse.

  ; ESC dismisses the inline views.

  ; As of Cursive 1.14.0-eap2, you can now create custom nodes in this tree.
  ; This uses tagged literals, see: https://clojure.org/reference/reader#tagged_literals

  ; Cursive already uses some tagged literals:
  ; https://cursive-ide.com/userguide/repl.html#showing-data-structure-diffs-from-the-repl
  ; e.g. eval this form:

  (tagged-literal 'cursive/diff {:title "Example diff"
                                 :left  (meta #'defn)
                                 :right (meta #'let)})

  ; Custom nodes are produced using tagged literals, using the cursive/node tag, like this:

  (tagged-literal 'cursive/node {:presentation [{:text  "Hello world"
                                                 :color :red}]})

  ; If you want to see what these look like under the hood, eval this form.

  (prn (tagged-literal 'cursive/node {:presentation [{:text  "Hello world"
                                                      :color :red}]}))

  ; That example is pretty simple, it's not expandable since it has no children.
  ; This one does:

  (tagged-literal 'cursive/node {:presentation [{:text  "Hello world"
                                                 :color :red}]
                                 :children     [(tagged-literal 'cursive/node {:presentation [{:text  "I'm a child"
                                                                                               :color :green}]})
                                                (tagged-literal 'cursive/node {:presentation [{:text  "Me too!"
                                                                                               :color :yellow}]})
                                                (tagged-literal 'cursive/node {:presentation [{:text  "And me!"
                                                                                               :color :teal}]})]})

  ; In that example, all the children are custom nodes, but you can also mix them
  ; with normal data. Notice how the data is interleaved with the custom nodes.

  (tagged-literal 'cursive/node {:presentation [{:text  "Hello world"
                                                 :color :red}]
                                 :children     [; This is a custom node like we saw above
                                                (tagged-literal 'cursive/node {:presentation [{:text  "I'm a child"
                                                                                               :color :green}]})
                                                (tagged-literal 'cursive/node {:presentation [{:text  "I'm a child"
                                                                                               :color :comment}]})
                                                ; These are normal data and use default inline rendering
                                                (meta #'defn)
                                                {:some   'more
                                                 :actual "data"}]})

  ; The tagged literals are pretty verbose, so cursive.inline.nodes contains some
  ; simple wrapper functions to make the creation of the nodes easier.

  ; Navigate here to take a look at them:
  cursive.inline.nodes

  ; Now just keep evaluating these forms

  (title-node "Styling nodes"
    (comment-node "; The presentation of a node is made up of a series of chunks, which can be styled individually.")
    (comment-node "; You can supply a colour and/or a style, both are optional.")

    (title-node "Standard colours from the IntelliJ UI Kit:"
      ; Notice that we can create the children as a seq, like in Reagent or Hiccup.
      (for [colour [:green :red :blue :yellow :orange :purple :teal :gray]]
        (node {:presentation (into []
                                   (comp
                                     (map #(if %
                                             {:text  (name %)
                                              :color colour
                                              :style %}
                                             {:text  (name colour)
                                              :color colour}))
                                     (interpose {:text " "}))
                                   [nil :italic :bold :underline :strikeout])})))
    (title-node "Special colours for imitating editor UI elements:"
      (for [colour [:error :inactive :link]]
        (node {:presentation (into []
                                   (comp
                                     (map #(if %
                                             {:text  (name %)
                                              :color colour
                                              :style %}
                                             {:text  (name colour)
                                              :color colour}))
                                     (interpose {:text " "}))
                                   [nil :italic :bold :underline :strikeout])}))))

  (title-node "Form colours"
    (comment-node "; There are also some options for styling elements like source code forms.")
    (comment-node "; Note that in this case, the :style is ignored, because it comes from the")
    (comment-node "; built-in style for that form type.")
    (node {:presentation [{:text  ":this"
                           :color :keyword}
                          {:text " looks like a keyword"}]})
    (node {:presentation [{:text  "this"
                           :color :symbol}
                          {:text " looks like a symbol"}]})
    (node {:presentation [{:text  "0"
                           :color :number}
                          {:text " looks like a number"}]})
    (node {:presentation [{:text  "\"this\""
                           :color :string}
                          {:text " looks like a string"}]})
    (node {:presentation [{:text  "[]"
                           :color :brace}
                          {:text " look like braces"}]})
    (node {:presentation [{:text  "()"
                           :color :paren}
                          {:text " look like parens"}]})
    (node {:presentation [{:text  "nil true false"
                           :color :literal}
                          {:text " look like literals"}]})
    (node {:presentation [{:text  "\\newline"
                           :color :char}
                          {:text " looks like a char"}]})
    (node {:presentation [{:text  "// this looks like a comment"
                           :color :comment}]}))


  (title-node "Test navigation"
    (comment-node "; We can also add actions to nodes, and this makes them interactive in the tree.")
    (comment-node "; Execute the action using either Enter, or by double clicking.")
    (node {:presentation [{:text  "Navigate to a Clojure file"
                           :color :link}]
           :action       :navigate
           :file         "cursive/inline/nodes.clj"
           :line         27
           :column       7})
    (node {:presentation [{:text  "Navigate to a Java class"
                           :color :link}]
           :action       :navigate
           :class        "some.JavaClass"
           :line         8
           :column       16})
    (comment-node "; I have styled these nodes like links to make it clear you can do something with")
    (comment-node "; them, but that's not required. Stacktrace elements in errors don't look like")
    (comment-node "; links, but you can still use them to navigate."))


  (title-node "Test browse"
    (comment-node "; You can also provide web links:")
    (node {:presentation [{:text  "Show me the doc"
                           :color :link}]
           :action       :browse
           :url          "https://cursive-ide.com/userguide/repl.html"})
    ; Or use the node functions
    (link-node "Show me the examples"
               "https://github.com/cursive-ide/inline-nodes"))

  ; Eval these in the REPL to define them.

  (defn print-it [params]
    (pp/pprint params))

  (defn return-it [params]
    params)

  (title-node "Test evaluation"
    (comment-node "; Now, we can create nodes which will invoke these vars.")
    (comment-node "; The :var item specifies a symbol which will be used to resolve the var to invoke.")
    (comment-node "; The whole data structure from the node will be passed to that var.")
    (node {:presentation [{:text  "Evaluate, and return the passed parameters"
                           :color :link}]
           :action       :eval
           :var          'cursive.tutorials.inline.a-start-here/return-it
           :this-is      {:some 'extra-data}})
    (node {:presentation [{:text  "Evaluate, and print out the passed parameters"
                           :color :link}]
           :action       :eval
           :var          'cursive.tutorials.inline.a-start-here/print-it
           :this-is      {:some 'extra-data}}))



  (title-node "You are done with the basics"
    (comment-node "; Now go to the next step to see how to put this all together in a real example.")
    (node {:presentation [{:text  "cursive.tutorials.inline.b-malli"
                           :color :link}]
           :action       :navigate
           :file         "cursive/tutorials/inline/b_malli.clj"
           :line         8
           :column       3})))
