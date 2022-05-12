package com.charts.bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charts.player.AudioPlayer
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun BarChartScreen(
    backgroundColor: Color = Color.White
) {
    var playSoundTimes by remember {
        mutableStateOf(0)
    }
    val values: List<Float> = listOf(10f, 50f, 60f, 10f, 50f, 60f, 30f)
    var index by remember {
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
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
                    .semantics {
                        contentDescription =
                            "Bar Chart with sales of the week, total value of 20.000,00 €. From January 1, 2022 to January 8, 2022."
                    }) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Sales this Week",
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
                    onClick = {
                        playSoundTimes += 1
                    }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_vector),
                        contentDescription = "Play the sound of Sales of This Week chart"
                    )
                }
            }

            BarChart(values, index, withTooltip) {
                index = it
            }
        }
    }

    if (playSoundTimes > 0) {
        val context = LocalContext.current
        LaunchedEffect(key1 = playSoundTimes) {
            val player = AudioPlayer(context)
            player.updateLowHighPoints(
                values.minOrNull()?.toDouble() ?: 0.0,
                values.maxOrNull()?.toDouble() ?: 0.0
            )
            withTooltip = false
            values.forEachIndexed { indexValue, value ->
                delay(500)
                index = indexValue
                player.onPointFocused(1.0, value.toDouble())
            }
            delay(500)
            withTooltip = true
            index = -1
            player.dispose()
        }
    }
}

@Composable
fun BarChart(
    values: List<Float> = listOf(10f, 50f, 60f, 10f, 50f, 60f, 30f),
    index: Int = -1,
    withTooltip: Boolean = true,
    onIndexChange: (Int) -> Unit = {}
) {
    val height = 160
    val maxValue = values.maxOrNull() ?: 0f
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
            Bar("M", "Monday", maxValue, height, values[0], index == 0, index == -1) {
                onIndexChange(0)
                tooltipTitle = "January 2, 2022"
                tooltipValue = "560,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", "Tuesday", maxValue, height, values[1], index == 1, index == -1) {
                onIndexChange(1)
                tooltipTitle = "January 3, 2022"
                tooltipValue = "4.010,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("W", "Wednesday", maxValue, height, values[2], index == 2, index == -1) {
                onIndexChange(2)
                tooltipTitle = "January 4, 2022"
                tooltipValue = "7.400,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("T", "Thursday", maxValue, height, values[3], index == 3, index == -1) {
                onIndexChange(3)
                tooltipTitle = "January 5, 2022"
                tooltipValue = "5.200,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("F", "Friday", maxValue, height, values[4], index == 4, index == -1) {
                onIndexChange(4)
                tooltipTitle = "January 6, 2022"
                tooltipValue = "7.499,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", "Saturday", maxValue, height, values[5], index == 5, index == -1) {
                onIndexChange(5)
                tooltipTitle = "January 7, 2022"
                tooltipValue = "3.600,00 €"
            }
            Spacer(modifier = Modifier.width(24.dp))
            Bar("S", "Sunday", maxValue, height, values[6], index == 6, index == -1) {
                onIndexChange(6)
                tooltipTitle = "January 8, 2022"
                tooltipValue = "2.510,30 €"
            }
        }

        BarTooltip(
            title = tooltipTitle,
            value = tooltipValue,
            index = index,
            visible = index >= 0 && withTooltip,
            onDismissRequest = { onIndexChange(-1) }
        )
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
    isNothingSelected: Boolean,
    onSelected: () -> Unit
) {
    val alpha by animateFloatAsState(targetValue = if (isSelected) 1f else 0f)
    val color by animateColorAsState(
        targetValue = if (isSelected || isNothingSelected) Color.Black else Color(0xFFE6E6E6)
    )
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
                    .background(color)
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

@Preview
@Composable
fun BarChartPreview() {
    BarChart()
}
