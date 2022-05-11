package com.charts.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarChartScreen(
    backgroundColor: Color = Color.White
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        Column {
            Row(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Total Sales",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    Text(
                        text = "20.000,00 €",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    Text(
                        text = "from 1 to 7, January 2022",
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

            BarChart()
        }
    }
}

@Composable
fun BarChart(
    values: List<Float> = listOf(10f, 50f, 60f, 10f, 50f, 60f, 30f)
) {
    val height = 160
    val maxValue = values.maxOrNull() ?: 0f
    Box(
        Modifier
            .padding(16.dp)
            .height(200.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            Modifier
                .align(Alignment.Center),
            verticalAlignment = Alignment.Bottom
        ) {
            Bar("M", "Monday", maxValue, height, values[0])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", "Tuesday", maxValue, height, values[1])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("W", "Wednesday", maxValue, height, values[2])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", "Thursday", maxValue, height, values[3])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("F", "Friday", maxValue, height, values[4])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", "Saturday", maxValue, height, values[5])
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", "Sunday", maxValue, height, values[6])
        }
    }
}

@Composable
fun Bar(
    text: String,
    fullText: String,
    maxValue: Float,
    height: Int,
    value: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics {
            stateDescription = "$fullText, $value"
        }
    ) {
        val barHeight = (value / maxValue) * height
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(barHeight.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black)
        )

        Spacer(Modifier.height(16.dp))
        Text(text = text, color = Color.Black)
    }
}

@Preview
@Composable
fun BarChartPreview() {
    BarChart()
}
