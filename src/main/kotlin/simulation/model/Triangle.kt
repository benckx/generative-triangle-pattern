package simulation.model

import kotlin.math.acos
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sign

data class Triangle(val a: Point, val b: Point, val c: Point) {

    val points: Set<Point>
        get() = setOf(a, b, c)

    val edges : Set<Edge>
        get() = setOf(Edge(a, b), Edge(b, c), Edge(c, a))

    /**
     * A point is inside a triangle if it has the same sign with respect to all three edges.
     * This method works by checking if the point lies on the same side of each of the triangle's edges.
     * The orientation is determined by the sign of the cross-product of the vectors formed by the triangle's vertices and the point.
     *
     * The cross-product of two 2D vectors (p1 - p0) and (p2 - p0) is given by:
     * cross_product = (p1.x - p0.x) * (p2.y - p0.y) - (p1.y - p0.y) * (p2.x - p0.x)
     *
     * If the point is inside the triangle, the cross products for all three edges will have the same sign (or be zero if the point is on an edge).
     */
    fun contains(point: Point): Boolean {
        fun crossProduct(p1: Point, p2: Point, p3: Point): Double {
            return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)
        }

        val sign1 = sign(crossProduct(a, b, point))
        val sign2 = sign(crossProduct(b, c, point))
        val sign3 = sign(crossProduct(c, a, point))

        val hasNegative = sign1 < 0 || sign2 < 0 || sign3 < 0
        val hasPositive = sign1 > 0 || sign2 > 0 || sign3 > 0

        return !(hasNegative && hasPositive)
    }

    fun angles(): List<Double> {
        val ab = hypot(b.x - a.x, b.y - a.y)
        val bc = hypot(c.x - b.x, c.y - b.y)
        val ca = hypot(a.x - c.x, a.y - c.y)

        val angleA = acos((ab.pow(2) + ca.pow(2) - bc.pow(2)) / (2 * ab * ca)) * (180 / Math.PI)
        val angleB = acos((ab.pow(2) + bc.pow(2) - ca.pow(2)) / (2 * ab * bc)) * (180 / Math.PI)
        val angleC = 180.0 - angleA - angleB

        return listOf(angleA, angleB, angleC)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Triangle
        return points == other.points
    }

    override fun hashCode(): Int = points.hashCode()
}
