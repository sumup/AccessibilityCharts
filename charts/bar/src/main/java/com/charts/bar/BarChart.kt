package com.charts.bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

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
                        contentDescription = null
                    )
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
    var index by remember {
        mutableStateOf(-1)
    }
    var tooltipTitle by remember {
        mutableStateOf("")
    }
    var tooltipValue by remember {
        mutableStateOf("")
    }

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
            Bar("M", "Monday", maxValue, height, values[0], index == 0) {
                index = 0
                tooltipTitle = "January 2, 2022"
                tooltipValue = "560,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", "Tuesday", maxValue, height, values[1], index == 1) {
                index = 1
                tooltipTitle = "January 3, 2022"
                tooltipValue = "4.010,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("W", "Wednesday", maxValue, height, values[2], index == 2) {
                index = 2
                tooltipTitle = "January 4, 2022"
                tooltipValue = "7.400,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", "Thursday", maxValue, height, values[3], index == 3) {
                index = 3
                tooltipTitle = "January 5, 2022"
                tooltipValue = "5.200,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("F", "Friday", maxValue, height, values[4], index == 4) {
                index = 4
                tooltipTitle = "January 6, 2022"
                tooltipValue = "7.499,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", "Saturday", maxValue, height, values[5], index == 5) {
                index = 5
                tooltipTitle = "January 7, 2022"
                tooltipValue = "3.600,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", "Sunday", maxValue, height, values[6], index == 6) {
                index = 6
                tooltipTitle = "January 8, 2022"
                tooltipValue = "2.510,30 €"
            }
        }

        AnimatedVisibility(visible = index >= 0, modifier = Modifier.align(Alignment.TopStart)) {
            BarTooltip(
                title = tooltipTitle,
                value = tooltipValue,
                index = index,
                modifier = Modifier.onFocusChanged {

                }
            )
        }
    }
}

@Composable
fun Bar(
    text: String,
    fullText: String,
    maxValue: Float,
    height: Int,
    value: Float,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val alpha by animateFloatAsState(targetValue = if (isSelected) 1f else 0f)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .semantics {
                contentDescription = "$fullText sales. Double click to open for more info."
            }
            .clickable {
                onSelected()
            }
    ) {
        val barHeight = (value / maxValue) * height

        Box {
            Box(
                Modifier
                    .width(2.dp)
                    .height(height.dp)
                    .alpha(alpha)
                    .background(Color.Black)
                    .align(Alignment.Center)
            )

            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(barHeight.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black)
                    .align(Alignment.BottomStart)
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(text = text, color = Color.Black, modifier = Modifier.clearAndSetSemantics { })
    }
}

@Composable
fun BarTooltip(
    title: String,
    value: String,
    index: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier
            .wrapContentSize()
            .padding(start = (index.absoluteValue * 24).dp)
            .clearAndSetSemantics { contentDescription = "teste" }
            .clickable { },
        border = BorderStroke(width = 1.dp, color = Color.Black)
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

@Preview
@Composable
fun BarChartPreview() {
    BarChart()
}
