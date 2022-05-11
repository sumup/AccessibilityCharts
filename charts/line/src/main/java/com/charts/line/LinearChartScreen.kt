package com.charts.line

import androidx.compose.runtime.Composable
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charts.axis.*

@Composable
fun LinearChartScreen(
    lineColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    data: List<Int>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8),
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Sales by hour",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    Text(
                        text = "7.000,00 €",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    Text(
                        text = "7, January 2022",
                        fontSize = 14.sp,
                        color = Color(0xFF999999),
                    )
                }

                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_vector),
                        contentDescription = null)
                }
            }

            LinearChart(
                modifier = Modifier
                    .height(170.dp)
                    .padding(16.dp)
                    .fillMaxWidth(),
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
    verticalOffset: Float = -5f,
    pointDrawer: PointDrawer = PointDrawer()
) {
    Canvas(modifier = modifier) {
        val distance = size.width / data.size
        var currentX = 0F
        val maxValue = data.maxOrNull() ?: 0
        val minValue = 0
        val points = mutableListOf<PointF>()

        drawIntoCanvas { canvas ->

            data.forEachIndexed { index, currentData ->
                if (data.size >= index + 1) {
                    val y = (maxValue - currentData) * (size.height / maxValue)
                    val x = currentX + distance
                    points.add(PointF(x, y))
                    currentX += distance

                    pointDrawer.drawPoint(
                        drawScope = this,
                        canvas = canvas,
                        center = Offset(currentX - 100, y - 40)
                    )
                }
            }

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

            drawLineChart(points, lineColor)

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
        labels = listOf("00", "06", "12", "18", "")
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
    lineColor: Color
) {
    for (i in 0 until points.size - 1) {
        drawLine(
            start = Offset(points[i].x - 100, points[i].y - 40),
            end = Offset(points[i + 1].x - 100, points[i + 1].y - 40),
            color = lineColor,
            strokeWidth = 8f
        )
    }
}