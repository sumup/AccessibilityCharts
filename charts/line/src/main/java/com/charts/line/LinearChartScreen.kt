package com.charts.line

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charts.axis.*
import com.charts.player.AudioPlayer
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun LinearChartScreen(
    lineColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    data: List<Int>) {

    var playSoundTimes by remember {
        mutableStateOf(0)
    }

    var pointClicked by remember {
        mutableStateOf(-1)
    }
    var withTooltip by remember {
        mutableStateOf(true)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8),
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .semantics {
                    contentDescription =
                        "Line Chart with Today’s Sales. January, seventh. 2022, total value of seven thousand €."
                }
        ) {
            Row(
                Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 32.dp)) {
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

            val dotRects = ArrayList<Rect>()
            dotRects.add(Rect(top = 0f, left = 0f, bottom = 360f, right = 180f))
            dotRects.add(Rect(top = 0f, left = 181f, bottom = 360f, right = 280f))
            dotRects.add(Rect(top = 0f, left = 350f, bottom = 360f, right = 420f))
            dotRects.add(Rect(top = 0f, left = 421f, bottom = 360f, right = 600f))
            dotRects.add(Rect(top = 0f, left = 601f, bottom = 360f, right = 800f))
            LineChart(
                modifier = Modifier
                    .height(170.dp)
                    .clickable {  }
                    .semantics {
                        contentDescription = """
                            Sales at 5 am, one thousand €.
                            Sales at 7 am, one thousand €.
                            Sales at 12 pm, three thousand €.
                            Sales at 16 pm, a thousand, seven hundred €.
                            Sales at 20 pm, a thousand, seven hundred €.
                        """.trimIndent()
                    }
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { tapOffset ->
                                dotRects.forEachIndexed { index, rect ->
                                    if (rect.contains(tapOffset)) {
                                        pointClicked = index
                                    }
                                }
                            }
                        )
                    }
                    .fillMaxWidth(),
                data = data,
                lineColor = lineColor,
                backgroundColor = backgroundColor,
                pointClicked = pointClicked,
                withTooltip = withTooltip,
                onDismissRequest = {
                    pointClicked = -1
                }
            )
        }
    }

    if (playSoundTimes > 0) {
        val context = LocalContext.current
        val player = AudioPlayer(context)
        LaunchedEffect(key1 = playSoundTimes) {
            player.updateLowHighPoints(data.minOrNull()?.toDouble() ?: 0.0, data.maxOrNull()?.toDouble() ?: 0.0)
            withTooltip = false

            data.forEachIndexed { index, value ->
                delay(500)
                pointClicked = index
                player.onPointFocused(1.0, value.toDouble())
            }

            delay(500)
            pointClicked = -1
            withTooltip = true
            player.dispose()
        }
    }
}

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: List<Int>,
    lineColor: Color,
    backgroundColor: Color,
    xAxis: XAxis = DefaultXAxis(),
    yAxis: YAxis = DefaultYAxis(),
    horizontalOffset: Float = 5f,
    verticalOffset: Float = -5f,
    pointDrawer: PointDrawer = PointDrawer(),
    pointClicked: Int = -1,
    withTooltip: Boolean = true,
    onDismissRequest: () -> Unit
) {
    Box {
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
                            center = Offset(currentX - 100, y - 40),
                            color = if (pointClicked == index || pointClicked == -1) Color.Black else Color(0xFFE6E6E6)
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
                drawYAxis(
                    yAxis,
                    yAxisDrawableArea,
                    yAxisLabelsDrawableArea,
                    minValue,
                    maxValue,
                    canvas
                )
            }
        }

        Tooltip(index = pointClicked, visible = pointClicked >= 0 && withTooltip) {
            onDismissRequest()
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

@Composable
fun Tooltip(
    index: Int,
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {
    var lastIndex by remember {
        mutableStateOf(-1)
    }

    if (index != -1) {
        lastIndex = index
    }

    val indexValue = if (index == -1) {
        lastIndex
    } else index

    var title = when(index) {
        0 -> "Sales on 05 hours"
        1 -> "Sales on 07 hours"
        2 -> "Sales on 12 hours"
        3 -> "Sales on 16 hours"
        4 -> "Sales on 20 hours"
        else -> ""
    }

    var value = when(index) {
        0 -> "1.000,00 €"
        1 -> "1.000,00 €"
        2 -> "3.000,00 €"
        3 -> "1.700,00 €"
        4 -> "1.700,00 €"
        else -> ""
    }

    DropdownMenu(
        expanded = visible,
        onDismissRequest = { onDismissRequest() },
        offset = DpOffset(x = (indexValue.absoluteValue * 32).dp, y = 0.dp),
        modifier = modifier
            .wrapContentSize()
            .clearAndSetSemantics {
                contentDescription = "Sales on $title. Value of $value. Double tap to close."
            }
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
            .background(Color.White)
            .clickable {
                onDismissRequest()
            }
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .width(120.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFF999999),
            )

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }
    }
}