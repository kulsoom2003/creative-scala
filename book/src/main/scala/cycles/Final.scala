package creativescala
package cycles

import cats.implicits._
import doodle.core._
import doodle.image._
import doodle.syntax.all._
import doodle.image.syntax.all._
import doodle.java2d._

object Final {

  def drawCurve(samples: Int, stop: Angle = Angle.one)(
      dot: Angle => Image
  )(f: Angle => Point): Image = {
    // Angle.one is one complete turn. I.e. 360 degrees
    val step = stop / samples
    def loop(count: Int): Image = {
      val angle = step * count
      count match {
        case 0 => Image.empty
        case n =>
          dot(angle).at(f(angle)).on(loop(n - 1))
      }
    }

    loop(samples)
  }

  def sampleCurve(samples: Int, stop: Angle = Angle.one)(
      curve: Angle => Point
  ): List[Point] = {
    // Angle.one is one complete turn. I.e. 360 degrees
    val step = stop / samples
    def loop(count: Int): List[Point] = {
      count match {
        case 0 => List.empty
        case n =>
          val angle = step * count
          curve(angle) :: loop(n - 1)
      }
    }

    loop(samples)
  }

  def scale(factor: Double): Point => Point =
    (point: Point) => Point(point.r * factor, point.angle)

  val parametricCircle: Angle => Point =
    (angle: Angle) => Point(1.0, angle)

  val parametricSpiral: Angle => Point =
    (angle: Angle) => Point(Math.exp(angle.toTurns - 1), angle)

  def lissajous(a: Int, b: Int, offset: Angle): Angle => Point =
    (angle: Angle) => Point(((angle * a) + offset).sin, (angle * b).sin)

  def rose(k: Int): Angle => Point =
    (angle: Angle) => {
      Point.cartesian((angle * k).cos * angle.cos, (angle * k).cos * angle.sin)
    }

  def wheel(speed: Int): Angle => Point =
    angle => Point(1.0, angle * speed)

  def on(curve1: Angle => Point, curve2: Angle => Point): Angle => Point =
    angle => curve1(angle) + curve2(angle).toVec

  val nSamples = 350

  Image
    .path(
      ClosedPath.interpolatingSpline(
        sampleCurve(nSamples)(
          on(
            lissajous(2, 3, 0.degrees).andThen(scale(150)),
            rose(7).andThen(scale(100))
          )
        )
      )
    )
    .strokeColor(Color.darkBlue)
    .save("cycles/lissajous-rose")

  Image
    .path(
      ClosedPath.interpolatingSpline(
        sampleCurve(nSamples)(
          on(
            wheel(5).andThen(scale(50)),
            lissajous(2, 3, 0.degrees).andThen(scale(150))
          )
        )
      )
    )
    .strokeColor(Color.darkBlue)
    .save("cycles/wheel-lissajous")

  drawCurve(60)(angle =>
    Image
      .circle(angle.toTurns * 24 + 2)
      .strokeColor(Color.blue.saturation(0.7.normalized).spin(angle * 0.2))
      .strokeWidth(3.0)
  )(
    on(
      wheel(5).andThen(scale(50)),
      lissajous(2, 3, 0.degrees).andThen(scale(150))
    )
  ).save("cycles/wheel-lissajous-circles")
}
