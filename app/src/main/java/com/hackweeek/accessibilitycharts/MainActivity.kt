package com.hackweeek.accessibilitycharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charts.bar.BarChart
import com.hackweeek.accessibilitycharts.ui.theme.AccessibilityChartsTheme
import com.hackweeek.line.LinearChartScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessibilityChartsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        LinearChartScreen()
                        Spacer(modifier = Modifier.height(16.dp))
                        BarChart()
                    }

                }
            }
        }
    }
}

