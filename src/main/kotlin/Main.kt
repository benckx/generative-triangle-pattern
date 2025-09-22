import model.Point
import model.Triangle

private const val MIN_DISTANCE = 200
private const val MAX_DISTANCE = 500

private const val WIDTH = 4_000
private const val HEIGHT = 4_000

private fun initTriangle(): Triangle {
    val p1 = Point(
        (0..MAX_DISTANCE).random().toDouble(),
        (0..MAX_DISTANCE).random().toDouble()
    )
    val p2 = Point(
        (0..MAX_DISTANCE).random().toDouble(),
        (0..MAX_DISTANCE).random().toDouble()
    )
    val p3 = Point(
        (0..MAX_DISTANCE).random().toDouble(),
        (0..MAX_DISTANCE).random().toDouble()
    )
    return Triangle(p1, p2, p3)
}

fun main() {
    val points = mutableListOf<Point>()
    val triangles = mutableListOf<Triangle>()

    val initTriangle = initTriangle()
    points += initTriangle.points
    triangles += initTriangle

    fun isTooCloseToAnotherPoint(candidate: Point) =
        points.any { it.distanceTo(candidate) < MIN_DISTANCE }

    fun isTooFarFromAllPoints(candidate: Point) =
        !points.isEmpty() && points.all { it.distanceTo(candidate) > MAX_DISTANCE }

    (1..200).forEach { _ ->
        var goOn = true
        while (goOn) {
            val newPoint = Point(
                (0..WIDTH).random().toDouble(),
                (0..HEIGHT).random().toDouble()
            )

            if (!isTooCloseToAnotherPoint(newPoint) &&
                !isTooFarFromAllPoints(newPoint) &&
                triangles.none { it.contains(newPoint) }
            ) {
                val closestTriangle = triangles.minBy { triangle ->
                    triangle.points.minOf { it.distanceTo(newPoint) }
                }

                val closest = closestTriangle.points.sortedBy { it.distanceTo(newPoint) }.take(2)
                val newTriangle = Triangle(closest[0], closest[1], newPoint)
                if (newTriangle.angles().min() >= 30) {
                    triangles += newTriangle
                    points += newPoint
                    goOn = false
                }
            }
        }
    }

    outputToPng(
        triangles = triangles,
        fillPolygons = true,
        fileName = "grow"
    )
}
