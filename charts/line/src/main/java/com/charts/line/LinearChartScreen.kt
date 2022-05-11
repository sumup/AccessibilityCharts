package com.charts.line

import androidx.compose.runtime.Composable
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.charts.axis.*

@Composable
fun LinearChartScreen(
    lineColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    data: List<Int>
) {
    Card(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(align = Alignment.TopStart)
        ) {
            LinearChart(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                data = data,
                lineColor = lineColor,
                backgroundColor = backgroundColor
            )
        }
    }
}

@Composable
fun LinearChart(
    modifier: Modifier = Modifier,
    data: List<Int>,
    lineColor: Color,
    backgroundColor: Color,
    xAxis: XAxis = DefaultXAxis(),
    yAxis: YAxis = DefaultYAxis(),
    horizontalOffset: Float = 5f,
    verticalOffset: Float = -5f
) {
    Canvas(modifier = modifier) {
        val distance = size.width / data.size
        var currentX = 0F
        val maxValue = data.maxOrNull() ?: 0
        val minValue = 0
        val points = mutableListOf<PointF>()

        data.forEachIndexed { index, currentData ->
            if (data.size >= index + 1) {
                val y = (maxValue - currentData) * (size.height / maxValue)
                val x = currentX + distance
                points.add(PointF(x, y))
                currentX += distance
            }
        }

        drawIntoCanvas { canvas ->
            val yAxisDrawableArea = calculateYAxisDrawableArea(
                xAxisLabelSize = xAxis.height(this),
                size = size
            )

            val xAxisDrawableArea = calculateXAxisDrawableArea(
                yAxisWidth = yAxisDrawableArea.width,
                labelHeight = xAxis.height(this),
                size = size
            )
            val xAxisLabelsDrawableArea = calculateXAxisLabelsDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                offset = horizontalOffset
            )
            val yAxisLabelsDrawableArea = calculateYAxisLabelsDrawableArea(
                yAxisDrawableArea = yAxisDrawableArea,
                offset = verticalOffset
            )
            val chartDrawableArea = calculateDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                yAxisDrawableArea = yAxisDrawableArea,
                size = size,
                offset = horizontalOffset
            )

            drawLineChart(points, lineColor, chartDrawableArea, canvas)

            drawXAxis(xAxis, xAxisDrawableArea, xAxisLabelsDrawableArea, canvas)
            drawYAxis(yAxis, yAxisDrawableArea, yAxisLabelsDrawableArea, minValue, maxValue, canvas)
        }
    }
}

private fun DrawScope.drawXAxis(xAxis: XAxis,
                                     xAxisDrawableArea: Rect,
                                     xAxisDrawableLabelArea: Rect,
                                     canvas: Canvas) {
    xAxis.drawAxisLine(
        drawScope = this,
        drawableArea = xAxisDrawableArea,
        canvas = canvas
    )

    xAxis.drawAxisLabels(
        drawScope = this,
        canvas = canvas,
        drawableArea = xAxisDrawableLabelArea,
        labels = listOf("S", "M", "T", "W", "T", "F", "S")
    )
}

private fun DrawScope.drawYAxis(yAxis: YAxis,
                                yAxisDrawableArea: Rect,
                                yAxisDrawableLabelArea: Rect,
                                minValue: Int,
                                maxValue: Int,
                                canvas: Canvas) {
    yAxis.drawAxisLine(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableArea
    )

    yAxis.drawAxisLabels(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableLabelArea,
        minValue = minValue.toFloat(),
        maxValue = maxValue.toFloat()
    )
}

private fun DrawScope.drawLineChart(
    points: MutableList<PointF>,
    lineColor: Color,
    drawableArea: Rect,
    canvas: Canvas
) {

    val axisLinePaint = Paint().apply {
        isAntiAlias = true
        color = lineColor
        style = PaintingStyle.Stroke
        strokeWidth = 8f
    }

    for (i in 0 until points.size - 1) {
//        canvas.drawLine(
//            p1 = Offset(
//                x = drawableArea.left - points[i].x,
//                y = drawableArea.left - points[i].y
//            ),
//            p2 = Offset(
//                x = points[i + 1].x,
//                y = points[i + 1].y
//            ),
//            paint = axisLinePaint
//        )

        drawLine(
            start = Offset(points[i].x - 100, points[i].y),
            end = Offset(points[i + 1].x - 100, points[i + 1].y),
            color = lineColor,
            strokeWidth = 8f
        )
    }
}