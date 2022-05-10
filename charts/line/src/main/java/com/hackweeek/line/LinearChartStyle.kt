package com.hackweeek.line

sealed class LinearChartStyle {
    object Default : LinearChartStyle()
    object Smooth : LinearChartStyle()
}