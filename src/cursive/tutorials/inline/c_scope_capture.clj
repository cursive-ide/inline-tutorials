(ns cursive.tutorials.inline.c-scope-capture
  (:require [cursive.inline.nodes :refer :all]
            [cursive.inline.scope-capture :refer [show-ep-info]]
            [sc.api :refer [spy]]))


; Now let's look at a more complex, interactive example. scope-capture
; (https://github.com/vvvvalvalval/scope-capture) is a library that helps
; debugging, using techniques that Stu Halloway discussed in a blog post:
; https://cognitect.com/blog/2017/6/5/repl-debugging-no-stacktrace-required.

; Essentially, you add a call to a `spy` macro in your code, and that will
; store the values of the current local variables in an atom somewhere. Then
; you can define vars which contain the captured values with the same names
; as the local variables, and then you can debug your function by evaluating
; expressions from it.

; Let's make some tooling around scope-capture, using examples from its doc.

; Again, start by loading this file into the REPL, then you can evaluate
; the forms in the (comment) blocks.

; Assume you need to debug a function with a bunch of locals:

(def my-fn
  (let [a 23
        b (+ a 3)]
    (fn [x y z]
      (let [u (inc x)
            v (+ y z u)]
        ; (spy)
        (* (+ x u a)
           (- v b))))))

(comment
  ; Let's try evaluating this function. You'll see you get the value -390,
  ; but the function is sufficiently complex that it's hard to see how you
  ; end up with that value.

  (my-fn 3 4 5)

  ; Let's get a better idea of how the function works. First, uncomment the
  ; (spy) call in the function definition above, and reload either the namespace
  ; or just the function definition. Then, evaluate the expression above again.
  ; You'll see some output come out, starting with something like: SPY [1 -1].
  ; The second number, which is always negative, is the code site, which
  ; corresponds to the location of the (spy) form when it was compiled. The
  ; first number, which is positive, is the execution ID. Each time you evaluate
  ; the function, a new execution ID will be assigned, capturing new values.

  ; Here's a call from the scope-capture API which will show the data it captured
  ; for the latest execution ID. You can see that it contains the names of the
  ; local variables, their captured values, as well as the source location of the
  ; code site.

  (sc.api/ep-info)

  ; The call below will take all the local variables and def them as vars. If your
  ; latest execution ID wasn't 1, change the argument here to your execution ID
  ; to use those values.

  ; Once you have those vars defined, explore the function above. I find it easiest
  ; to evaluate a particular expression by selecting it and then evaluating, but
  ; you could use eval form before caret too. Try selecting individual variables
  ; (like `a`, `x`, `u` etc) and you'll be able to see the values that were captured
  ; for them. You can also evaluate expressions using those vars, like `(+ a 3)`,
  ; `(+ y z u)` or any arbitrary expression that helps you figure the function out.

  (sc.api/defsc 1)

  ; Once you're done with that example, you can clear out the captures using this:

  (sc.api/dispose-all!))


; Now, let's actually debug something. Here's an example from the scope-capture doc.
; It's a hairy function that computes the distance between 2 points based on their
; coordinates, using the Haversine formula:

(defn haversine
  [x]
  (let [s (Math/sin (/ (double x) 2.0))]
    (* s s)))

(def distance
  (let [earth-radius 6.371e6
        radians-per-degree (/ Math/PI 180.0)]
    (fn [p1 p2]
      (let [[lat1 lng1] p1
            [lat2 lng2] p1
            phi1 (* lat1 radians-per-degree)
            lambda1 (* lng1 radians-per-degree)
            phi2 (* lat2 radians-per-degree)
            lambda2 (* lng2 radians-per-degree)]
        ; (spy)
        (* 2 earth-radius
           (Math/asin
             (Math/sqrt
               (+
                 (haversine (- phi2 phi1))
                 (*
                   (Math/cos phi1)
                   (Math/cos phi2)
                   (haversine (- lambda2 lambda1)))))))))))

(def Paris [48.8566 2.3522])
(def New-York [40.7134 -74.0055])
(def Athens [37.9838 23.7275])



(comment
  ; If you evaluate these, it returns 0 for all distances, which is clearly wrong.

  (distance Paris New-York)

  (distance Paris Athens)

  (distance New-York Athens)

  ; Let's debug this. Uncomment the (spy) form in the function above, reload it and then
  ; evaluate one of the distance functions above. Again, you'll get some info printed out,
  ; and then you can use ep-info as before to see the data:

  (sc.api/ep-info)

  ; If we dig around a bit, we can figure out that this is the atom that scope-capture
  ; stores its data in. If you evaluate this, you can see all the data it's capturing.
  ; This is the data we'll use to make a UI we can invoke.

  @sc.impl.db/db

  ; By examining the data from the atom above, we can create the following function,
  ; which is in the cursive.inline.scope-capture ns. Navigate to it to see how it works,
  ; or evaluate it to see the result.

  ; This returns a list of all the EPs, so if you only have one, evaluate the three distance
  ; functions above. You'll get a list of all the executions, with a little menu for each.
  ; You can inspect the captured data, or use the Take me! link to jump to the (spy) form.
  ; You can also use the Def them all! link to define the vars from that execution, and then
  ; examine the function as before to try to identify the bug.

  ; In the function definition, you can see how eval is used. The ep-id is passed to the
  ; function based on the position in the UI, so the evaled function can create the vars
  ; for the EP the user has selected.

  (show-ep-info)

  ; Choose one of your evaluations, then execute the Def them all! entry to create the vars.
  ; Then, use Take me! to jump to the (spy) form and figure out the problem. By evaluating
  ; the locals and expressions in the function, you should be able to figure out the bug.
  ; (Cursive makes the bug pretty obvious by marking the unused local in the editor, but work
  ; with me here...)

  ; Again, the best way to invoke this function is to use a REPL command. Frequently, data
  ; will be captured far from where you are in your editor. It might be captured by a test
  ; invocation, or an HTTP handler receiving a request. By binding this to a key, you can
  ; quickly inspect the returned values, def them as vars and then jump to the spy point
  ; to debug the function.

  ; At the risk of belabouring the point - Cursive doesn't know anything about scope capture.
  ; It's just showing the data that you give it in the way you have configured it.





  #_nil)
