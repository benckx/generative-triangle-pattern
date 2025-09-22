package model

data class Edge(val p1: Point, val p2: Point) {
    val points
        get() = setOf(p1, p2)

    fun shift(dx: Double, dy: Double): Edge =
        Edge(p1.shift(dx, dy), p2.shift(dx, dy))

    override fun toString(): String {
        return "Edge(x1=${p1.x}, y1=${p1.y}, x2=${p2.x}, y2=${p2.y})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        (other as Edge)
        return other.points == points
    }

    override fun hashCode(): Int = p1.hashCode() + p2.hashCode()

}
