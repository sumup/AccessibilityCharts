package com.charts.line

import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charts.axis.*
import com.charts.player.AudioPlayer
import kotlinx.coroutines.delay

@Composable
fun LinearChartScreen(
    lineColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    data: List<Int>,
) {

    var playSoundTimes by remember {
        mutableStateOf(0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8),
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(8.dp)
                .semantics {
                    contentDescription =
                        "Line Chart with Today’s Sales. January 7th. 2022, total value of 7.000,00 €."
                }
        ) {
            Row(Modifier.padding(start = 16.dp, top = 16.dp, bottom = 32.dp)) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Sales Today",
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
                    onClick = {
                        playSoundTimes += 1
                    },
                    modifier = Modifier.semantics {
                        contentDescription = "Play the sound of Today’s Sales chart."
                    }
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
                    .fillMaxWidth()
                    .semantics {
                               contentDescription = ""
                    },
                data = data,
                lineColor = lineColor,
                backgroundColor = backgroundColor
            )
        }
    }

    if (playSoundTimes > 0) {
        val context = LocalContext.current
        val player = AudioPlayer(context)
        LaunchedEffect(key1 = playSoundTimes) {
            player.updateLowHighPoints(data.minOrNull()?.toDouble() ?: 0.0, data.maxOrNull()?.toDouble() ?: 0.0)

//            player.playSummaryAudio(1.0, data.map { it.toDouble() })

            data.forEach { value ->
                delay(500)

                Log.d("HSS", "value is $value")

                player.onPointFocused(1.0, value.toDouble())
            }
            delay(4000)
            player.dispose()
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