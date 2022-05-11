package com.charts.axis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

fun Density.calculateYAxisDrawableArea(
    xAxisLabelSize: Float,
    size: Size
): Rect {
    // Either 400dp or 90% of the chart width.
    val right = minOf(400.dp.toPx(), size.width * 90f / 100f)

    return Rect(
        left = 0f,
        top = 0f,
        bottom = size.height - xAxisLabelSize,
        right = right
    )
}

fun calculateXAxisDrawableArea(
    yAxisWidth: Float,
    labelHeight: Float,
    size: Size
): Rect {
    val top = size.height - labelHeight

    return Rect(
        left = 0.0f,
        top = top,
        bottom = size.height,
        right = size.width - (labelHeight * 2) - 8
    )
}

fun calculateYAxisLabelsDrawableArea(
    yAxisDrawableArea: Rect,
    offset: Float
): Rect {
    val verticalOffset = yAxisDrawableArea.width * offset / 100f

    return Rect(
        left = yAxisDrawableArea.left + verticalOffset,
        top = yAxisDrawableArea.top,
        bottom = yAxisDrawableArea.bottom,
        right = yAxisDrawableArea.right - verticalOffset
    )
}

fun calculateXAxisLabelsDrawableArea(
    xAxisDrawableArea: Rect,
    offset: Float
): Rect {
    val horizontalOffset = xAxisDrawableArea.width * offset / 100f

    return Rect(
        left = xAxisDrawableArea.left + horizontalOffset,
        top = xAxisDrawableArea.top,
        bottom = xAxisDrawableArea.bottom,
        right = xAxisDrawableArea.right - horizontalOffset
    )
}

fun calculateDrawableArea(
    xAxisDrawableArea: Rect,
    yAxisDrawableArea: Rect,
    size: Size,
    offset: Float
): Rect {
    val horizontalOffset = xAxisDrawableArea.width * offset / 100f

    return Rect(
        left = yAxisDrawableArea.right + horizontalOffset,
        top = 0f,
        bottom = xAxisDrawableArea.top,
        right = size.width - horizontalOffset
    )
}