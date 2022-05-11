package com.charts.axis

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

interface XAxis {
    fun height(drawScope: DrawScope): Float

    fun drawAxisLine(
        drawScope: DrawScope,
        canvas: Canvas,
        drawableArea: Rect
    )

    fun drawAxisLabels(
        drawScope: DrawScope,
        canvas: Canvas,
        drawableArea: Rect,
        labels: List<String>
    )
}

class DefaultXAxis(
    private val axisLineThickness: Dp = 1.dp,
    private val axisLineColor: Color = Color.Black,
    private val labelTextSize: TextUnit = 12.sp,
    private val labelTextColor: Color = Color.Black,
    private val labelRatio: Int = 1
) : XAxis {

    private val axisLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor
        style = PaintingStyle.Stroke
    }

    private val textPaint = android.graphics.Paint().apply {
        isAntiAlias = true
        color = labelTextColor.toLegacyInt()
    }

    override fun height(drawScope: DrawScope): Float = with(drawScope) {
        (30f / 2f) * axisLineThickness.toPx()
    }

    override fun drawAxisLine(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect) =
        with(drawScope) {
            val lineThickness = axisLineThickness.toPx()
            val y = drawableArea.top + (lineThickness / 2f)

            canvas.drawLine(
                p1 = Offset(
                    x = drawableArea.left,
                    y = y
                ),
                p2 = Offset(
                    x = drawableArea.right,
                    y = y
                ),
                paint = axisLinePaint.apply {
                    strokeWidth = lineThickness
                }
            )
        }

    override fun drawAxisLabels(
        drawScope: DrawScope,
        canvas: Canvas,
        drawableArea: Rect,
        labels: List<String>
    ) {
        with(drawScope) {
            val labelPaint = textPaint.apply {
                textSize = labelTextSize.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
            }

            val labelIncrements = drawableArea.width / (labels.size - 1)
            labels.forEachIndexed { index, label ->
                if (index.rem(labelRatio) == 0) {
                    val x = drawableArea.left + (labelIncrements * (index))
                    val y = drawableArea.bottom

                    canvas.nativeCanvas.drawText(label, x, y, labelPaint)
                }
            }
        }
    }
}