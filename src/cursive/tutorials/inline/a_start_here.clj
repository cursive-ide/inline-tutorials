(ns cursive.tutorials.inline.a-start-here
  (:require [cursive.inline.nodes :refer :all]))

; To try this out, start the REPL and then load this file into it. You can then evaluate
; each form from the comment block below to see how it behaves.

(comment
  ; Custom nodes are produced using tagged literals, using the cursive/node tag, like this:

  (tagged-literal 'cursive/node {:presentation [{:text  "Hello world"
                                                 :color :red}]})

  ; This will produce a tagged literal, but you will only see its printed form in the output
  ; of the REPL panel. In the editor Cursive will intercept it to create the inline tree.
  ; Here's a form you can evaluate to see what they look like.

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
  ; with actual data, like this:

  (tagged-literal 'cursive/node {:presentation [{:text  "Hello world"
                                                 :color :red}]
                                 :children     [(tagged-literal 'cursive/node {:presentation [{:text  "I'm a child"
                                                                                               :color :green}]})
                                                (meta #'defn)
                                                {:some   'more
                                                 :actual "data"}]})

  ; The tagged literals are pretty verbose, so cursive.inline.nodes contains some
  ; simple wrapper functions to make the creation of the nodes easier. I hesitate to
  ; call it a DSL, since it's 4 functions and 30 lines of code, but feel free if you
  ; want to. We'll use those functions from now on.

  ; Ctrl/Cmd-click here to take a look at them:
  cursive.inline.nodes

  ; The presentation of a node is made up of a series of chunks, which can be styled
  ; individually. You can supply a colour and/or a style, both are optional.
  ; Here are the available colours, from the IntelliJ UI Kit at:
  ; https://www.figma.com/design/UowbJhRZZgcqa5Wmb4npee/Int-UI-Kit-(Community)?node-id=305-26005&node-type=canvas

  ; Notice that we can create the children as a seq, like in Reagent or Hiccup.
  ; Since this node contains more children than will be automatically expanded by
  ; default, you might need to expand the tree with the right arrow key.

  (title-node "Test colours"
    (for [color [:green :red :blue :yellow :orange :purple :teal :gray]]
      (node {:presentation [{:text  (name color)
                             :color color}
                            {:text " "}
                            {:text  "italic"
                             :color color
                             :style :italic}
                            {:text " "}
                            {:text  "bold"
                             :color color
                             :style :bold}
                            {:text " "}
                            {:text  "underline"
                             :color color
                             :style :underline}
                            {:text " "}
                            {:text  "strikeout"
                             :color color
                             :style :strikeout}]})))

  ; There are also some special colours, useful for UI elements.

  (title-node "UI colours"
    (for [color [:error :inactive :link]]
      (node {:presentation [{:text  (name color)
                             :color color}
                            {:text " "}
                            {:text  "italic"
                             :color color
                             :style :italic}
                            {:text " "}
                            {:text  "bold"
                             :color color
                             :style :bold}
                            {:text " "}
                            {:text  "underline"
                             :color color
                             :style :underline}
                            {:text " "}
                            {:text  "strikeout"
                             :color color
                             :style :strikeout}]})))

  ; There are also some options for styling elements like source code forms.
  ; Note that in this case, the :style is ignored, because it comes from the
  ; built-in style for that form type.

  (title-node "Form colours"
    (node {:presentation [{:text  "// this looks like a comment"
                           :color :comment}]})
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
                          {:text " looks like a char"}]}))

  ; We can also add actions to nodes, and this makes them interactive in the tree.
  ; Here we show that you can create nodes that navigate, either to a Java class
  ; (useful for stacktraces etc) or to a file, which you would normally use for
  ; Clojure. You can execute the action using either Enter if you're navigating
  ; in the Seeker with the keyboard, or by double clicking.

  ; I have styled these nodes like links to make it clear you can do something with
  ; them, but that's not required. Stacktrace elements in errors don't look like
  ; links, but you can still use them to navigate.

  (title-node "Test navigate"
    (node {:presentation [{:text  "Take me"
                           :color :link}]
           :action       :navigate
           :class        "some.JavaClass"
           :line         8
           :column       16})
    (node {:presentation [{:text  "Even better"
                           :color :link}]
           :action       :navigate
           :file         "cursive/inline/nodes.clj"
           :line         23
           :column       7}))

  ; This navigation is really useful for things like stacktraces, or any type
  ; of data where you have source code references - we'll see some examples later.

  ; You can also provide web links:

  (title-node "Test browse"
    (node {:presentation [{:text  "Show me the doc"
                           :color :link}]
           :action       :browse
           :url          "https://cursive-ide.com/userguide/repl.html"})
    ; This can be made nicer using the node functions:
    (link-node "Show me the examples"
               "https://github.com/cursive-ide/inline-nodes"))

  ; Finally, you can also create nodes that will execute vars in the REPL.

  ; First, let's define a function to call. It receives a single data structure
  ; as a parameter, and will print it out to stdout and then return it:

  (defn print-it [params]
    (pr params)
    params)

  ; Now, we can create a node which will invoke this var. The :var item
  ; specifies a symbol which will be used to resolve the var to invoke, and then
  ; the whole data structure from the node will be passed to that var. In
  ; this example, all the data is related to the representation of the node
  ; itself, but you can put whatever you want in there, IDs, paths to data, etc,
  ; anything the function needs to use to know what to do.

  ; Sometimes the printed output is completely covered by the horizontal scrollbar
  ; in the stdout view, you can drag it a bit bigger if that happens. I'm working
  ; on fixing that...

  (title-node "Test exec"
    (node {:presentation [{:text  "Do it!"
                           :color :link}]
           :action       :eval
           :var          'cursive.tutorials.inline.a-start-here/print-it}))

  ; That's it for the basic functionality. Now open cursive.tutorials.inline.b-malli
  ; to see how to put this all together in a real example.

  ; Ctrl/Cmd-click this to go there:
  cursive.tutorials.inline.b-malli





  ; Keep this closing paren down here if you're using parinfer...
  #_nil)
