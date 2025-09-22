package model

import kotlin.math.hypot

data class Point(
    val x: Double,
    val y: Double
) {

    fun shift(dx: Double, dy: Double): Point =
        Point(x + dx, y + dy)

    fun distanceTo(other: Point): Double =
        distanceBetween(this, other)

    companion object {

        fun distanceBetween(p1: Point, p2: Point): Double =
            hypot(p1.x - p2.x, p1.y - p2.y)

    }

}
