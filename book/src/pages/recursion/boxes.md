# A Line of Boxes

```scala mdoc:invisible
import doodle.core.*
import doodle.image.*
import doodle.syntax.all.*
import doodle.image.syntax.all.*
import doodle.java2d.*
```

Let's start with an example, drawing a line or row of boxes like in the image below.

@:figure{ img = "sequential-boxes.svg", key = "#fig:recursion:sequential-boxes", caption = "Five boxes filled with Royal Blue" }

Let's define a box to begin with.

```scala mdoc:silent
val aBox = Image.square(20).fillColor(Color.royalBlue)
```

Then one box in a row is just

```scala mdoc:silent
val oneBox = aBox
```

If we want to have two boxes side by side, that is easy enough.

```scala mdoc:silent
val twoBoxes = aBox.beside(oneBox)
```

Similarly for three.

```scala mdoc:silent
val threeBoxes = aBox.beside(twoBoxes)
```

And so on for as many boxes as we care to create.

You might think this is an unusual way to create these images.
Why not just write something like this, for example?

```scala mdoc:reset:invisible
import doodle.core.*
import doodle.image.*
import doodle.syntax.all.*
import doodle.image.syntax.all.*
import doodle.java2d.*
val aBox = Image.square(20).fillColor(Color.royalBlue)
val oneBox = aBox
val twoBoxes = aBox.beside(oneBox)
```
```scala mdoc:silent
val threeBoxes = aBox.beside(aBox).beside(aBox)
```

These two definitions are equivalent, as we can see from substitution.
We've chosen to write later images in terms of earlier ones to emphasise the structure we're dealing with, which is building up to structural recursion.

Writing images in this way could get very tedious.
What we'd really like is some way to tell the computer the number of boxes we'd like.
More technically, we would like to abstract over the expressions above.
We learned in the previous chapter that methods abstract over expressions, so let's try to write a method to solve this problem.

We'll start by writing a method skeleton that defines, as usual, what goes into the method and what it evaluates to.
In this case we supply an `Int` `count`, which is the number of boxes we want, and we get back an `Image`.

```scala mdoc:silent
def boxes(count: Int): Image =
  ???
```

Now comes the new part, the *structural recursion*.
We noticed that `threeBoxes` above is defined in terms of `twoBoxes`, and `twoBoxes` is itself defined in terms of `box`.
We could even define `box` in terms of *no* boxes, like so:

```scala mdoc:reset:invisible
import doodle.core.*
import doodle.image.*
import doodle.syntax.all.*
import doodle.image.syntax.all.*
import doodle.java2d.*
val aBox = Image.square(20).fillColor(Color.royalBlue)
```
```scala mdoc:silent
val oneBox = aBox.beside(Image.empty)
```

Here we used `Image.empty` to represent no boxes.

Imagine we had already implemented the `boxes` method.
We can say the following properties of `boxes` always hold, if it is correctly implemented:

- `boxes(0) == Image.empty`
- `boxes(1) == aBox.beside(boxes(0))`
- `boxes(2) == aBox.beside(boxes(1))`
- `boxes(3) == aBox.beside(boxes(2))`

The last three properties all have the same general shape.
We can describe all of them, and any case for `n > 0`, with the single property `boxes(n) == aBox.beside(boxes(n - 1))`.
So we're left with two properties

- `boxes(0) == Image.empty`
- `boxes(n) == aBox.beside(boxes(n-1))`

These two properties completely define the behavior of `boxes`.
In fact we can implement `boxes` by converting these properties into code.

A full implementation of `boxes` is

```scala mdoc:reset:invisible
import doodle.core.*
import doodle.image.*
import doodle.syntax.all.*
import doodle.image.syntax.all.*
import doodle.java2d.*
val aBox = Image.square(20).fillColor(Color.royalBlue)
```
```scala mdoc:silent
def boxes(count: Int): Image =
  count match {
    case 0 => Image.empty
    case n => aBox.beside(boxes(n-1))
  }
```

Try it and see what results you get!
This implementation is only a tiny bit more verbose than the properties we wrote above, and is our first structural recursion over the natural numbers.

At this point we have two questions to answer.
Firstly, how does this `match` expression work?
More importantly, is there some general principle we can use to create methods like this on our own?
Let's take each question in turn.

@:exercise(Stacking Boxes)

Even before we get into the details of `match` expressions you should be able to modify `boxes` to produce an image like the one below.

At this point we're trying to get used to the syntax of `match`, so rather than copying and pasting `boxes` write it all out by hand again to get some practice.

@:figure{ img = "sequential-boxes.svg", key = "#fig:recursion:stacked-boxes", caption = "Three stacked boxes filled with Royal Blue" }
@:@

@:solution
All you to do is change `beside` to `above` in `boxes`.

```scala mdoc:silent
def stackedBoxes(count: Int): Image =
  count match {
    case 0 => Image.empty
    case n => aBox.above(stackedBoxes(n-1))
  }
```
@:@
