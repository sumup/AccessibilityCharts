package com.charts.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PointDrawer(
    val diameter: Dp = 8.dp,
    val color: Color = Color.Black
) {

    private val paint = Paint().apply {
        color = this@PointDrawer.color
        style = PaintingStyle.Fill
        isAntiAlias = true
    }

    fun drawPoint(
        drawScope: DrawScope,
        canvas: Canvas,
        center: Offset
    ) {
        with(drawScope as Density) {
            canvas.drawCircle(center, diameter.toPx() / 2f, paint)
        }
    }
}