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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.charts.line.LinearChartStyle.*

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
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(align = Alignment.TopStart)
        ) {
            LinearChart(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                data = data,
                style = Default,
                lineColor = lineColor
            )
        }
    }
}

@Composable
fun LinearChart(
    modifier: Modifier = Modifier,
    style: LinearChartStyle = Default,
    data: List<Int>,
    lineColor: Color
) {
    Canvas(modifier = modifier) {
        val distance = size.width / (data.size + 1)
        var currentX = 0F
        val maxValue = data.maxOrNull() ?: 0
        val points = mutableListOf<PointF>()

        data.forEachIndexed { index, currentData ->
            if (data.size >= index + 2) {
                val y0 = (maxValue - currentData) * (size.height / maxValue)
                val x0 = currentX + distance
                points.add(PointF(x0, y0))
                currentX += distance
            }
        }

        if (style == Default) {
            drawDefaultLineChart(points, lineColor)
        } else {
            drawSmoothLineChart(points, lineColor)
        }
    }
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