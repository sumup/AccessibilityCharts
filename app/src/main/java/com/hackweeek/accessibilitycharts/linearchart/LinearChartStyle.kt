package com.hackweeek.accessibilitycharts.linearchart

sealed class LinearChartStyle {
    object Default : LinearChartStyle()
    object Smooth : LinearChartStyle()
}