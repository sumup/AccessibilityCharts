package com.charts.circle

import androidx.annotation.DrawableRes

data class CircleChartData(
    val title: String,
    val circleA: CircleData,
    val circleB: CircleData
) {
    fun largeCircle(): CircleData = if (circleA.value >= circleB.value) circleA else circleB
    fun smallCircle(): CircleData = if (circleA.value <= circleB.value) circleA else circleB
    fun largeRatio(): Float = largeCircle().value / largeCircle().value
    fun smallRatio(): Float = 1.75f * (smallCircle().value / largeCircle().value)
}

data class CircleData(
    val title: String,
    val value: Float,
    @DrawableRes val backgroundRes: Int
)