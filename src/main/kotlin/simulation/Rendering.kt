package simulation

import io.github.oshai.kotlinlogging.KotlinLogging
import simulation.model.Edge
import simulation.model.Triangle
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO

private val logger = KotlinLogging.logger {}

fun outputToPng(
    triangles: List<Triangle>,
    fillPolygons: Boolean = false,
    edgeColor: Color = Color.DARK_GRAY,
    edgeStroke: Float = 12f,
    subDirectory: String? = null,
    fileName: String = "layout"
) {
    val layoutPoints = triangles.flatMap { it.points }.distinct()
    val minX = layoutPoints.minOf { it.x }
    val maxX = layoutPoints.maxOf { it.x }
    val minY = layoutPoints.minOf { it.y }
    val maxY = layoutPoints.maxOf { it.y }

    // Calculate dynamic image dimensions with padding
    val padding = 50.0
    val width = (maxX - minX + 2 * padding).toInt()
    val height = (maxY - minY + 2 * padding).toInt()

    // Calculate offsets to center content
    val offsetX = padding - minX
    val offsetY = padding - minY

    logger.debug { "offset: ${offsetX}x${offsetY}" }

    // image
    val image = BufferedImage(width, height, TYPE_INT_RGB)
    val graphics = image.createGraphics()

    // anti-aliasing
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

    // background color
    graphics.color = Color.BLACK
    graphics.fillRect(0, 0, width, height)

    // fill polygons
    if (fillPolygons) {
        triangles.forEach { triangle ->
            graphics.color = Color.RED
            val orderedPoints = triangle.points.map { point -> point.shift(offsetX, offsetY) }
            val xPoints = orderedPoints.map { it.x.toInt() }.toIntArray()
            val yPoints = orderedPoints.map { it.y.toInt() }.toIntArray()
            graphics.fillPolygon(xPoints, yPoints, orderedPoints.size)
        }
    }

    // draw triangles edges
    graphics.color = edgeColor
    graphics.stroke = BasicStroke(edgeStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    triangles.flatMap { it.edges }.distinct()
        .map { edge -> edge.shift(offsetX, offsetY) }
        .forEach { edge -> graphics.drawEdge(edge) }

    graphics.dispose()

    // directories
    val outputDir = File("output")
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    val outputFile = if ((subDirectory) != null) {
        val subDir = File(outputDir, subDirectory)
        if (!subDir.exists()) {
            subDir.mkdirs()
        }
        File(subDir, "$fileName.png")
    } else {
        File(outputDir, "$fileName.png")
    }

    // write
    ImageIO.write(image, "PNG", outputFile)
}

private fun Graphics2D.drawEdge(edge: Edge) {
    this.drawLine(
        edge.p1.x.toInt(),
        edge.p1.y.toInt(),
        edge.p2.x.toInt(),
        edge.p2.y.toInt()
    )
}
