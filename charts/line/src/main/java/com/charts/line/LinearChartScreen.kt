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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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
    horizontalOffset: Float = 5f
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
            val chartDrawableArea = calculateDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                yAxisDrawableArea = yAxisDrawableArea,
                size = size,
                offset = horizontalOffset
            )

            drawDefaultLineChart(points, lineColor)

            drawXAndYAxis(xAxis, yAxis,
                xAxisDrawableArea, yAxisDrawableArea,
                xAxisLabelsDrawableArea,
                maxValue, minValue,
                canvas)
        }
    }
}

private fun DrawScope.drawXAndYAxis(xAxis: XAxis, yAxis: YAxis,
                                    xAxisDrawableArea: Rect,
                                    yAxisDrawableArea: Rect,
                                    xAxisDrawableLabelArea: Rect,
                                    maxValue: Int,
                                    minValue: Int,
                                    canvas: Canvas) {
    // Draw the X Axis line.
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

    // Draw the Y Axis line.
    yAxis.drawAxisLine(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableArea
    )

    yAxis.drawAxisLabels(
        drawScope = this,
        canvas = canvas,
        drawableArea = yAxisDrawableArea,
        minValue = minValue.toFloat(),
        maxValue = maxValue.toFloat()
    )
}

private fun DrawScope.drawSmoothLineChart(
    points: MutableList<PointF>,
    lineColor: Color
) {
    val cubicPoints1 = mutableListOf<PointF>()
    val cubicPoints2 = mutableListOf<PointF>()

    for (i in 1 until points.size) {
        cubicPoints1.add(PointF((points[i].x + points[i - 1].x) / 2, points[i - 1].y))
        cubicPoints2.add(PointF((points[i].x + points[i - 1].x) / 2, points[i].y))
    }

    val path = Path()
    path.moveTo(points.first().x, points.first().y)

    for (i in 1 until points.size) {
        path.cubicTo(
            cubicPoints1[i - 1].x,
            cubicPoints1[i - 1].y,
            cubicPoints2[i - 1].x,
            cubicPoints2[i - 1].y,
            points[i].x,
            points[i].y
        )
    }

    drawPath(path, color = lineColor, style = Stroke(width = 8f))
}

private fun DrawScope.drawDefaultLineChart(
    points: MutableList<PointF>,
    lineColor: Color
) {
    for (i in 0 until points.size - 1) {
        drawLine(
            start = Offset(points[i].x, points[i].y),
            end = Offset(points[i + 1].x, points[i + 1].y),
            color = lineColor,
            strokeWidth = 8f
        )
    }
}