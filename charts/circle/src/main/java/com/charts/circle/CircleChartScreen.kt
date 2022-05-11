package com.charts.circle

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircleChartScreen(
    lineColor: Color = Color.Black,
    fillColor: Color = Color.White,
    backgroundColor: Color = Color.White,
    circleChartData: CircleChartData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .semantics {
                contentDescription = "Gráfico best x least seller"
            },
        elevation = 10.dp,
        backgroundColor = backgroundColor
    ) {
        CircleChart(
            lineColor = lineColor,
            fillColor = fillColor,
            circleChartData = circleChartData
        )
    }
}

@Composable
fun CircleChart(
    lineColor: Color,
    fillColor: Color,
    circleChartData: CircleChartData
) {
    var isBrokenHeartVisible by remember { mutableStateOf(false) }
    var isHeartVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(Modifier.padding(16.dp)) {
        Row {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = circleChartData.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(
                onClick = {
                    Toast
                        .makeText(context, "play na musica", Toast.LENGTH_SHORT)
                        .show()
                },
                Modifier.semantics {
                    contentDescription = "Botão de tocar música"
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_music),
                    contentDescription = null
                )
            }
        }
        Box(
            modifier = Modifier.semantics(mergeDescendants = true) {
                contentDescription = "Descrição do gráfico: O círculo maior em cor preta repesenta o produto mais vendido, o cappuccino, sendo 40 unidades vendidas. O círculo menor em cor branca e borda preta representa o menos vendido, o pão de alho, sendo 8 unidades vendidas."
            }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clearAndSetSemantics { }
            ) {
                Text(
                    text = circleChartData.largeCircle().title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Row {
                    Text(
                        text = circleChartData.largeCircle().value.toInt().toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    val alpha by animateFloatAsState(
                        animationSpec = tween(durationMillis = 250),
                        targetValue = if (isHeartVisible) 1f else 0f
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = null,
                        Modifier
                            .padding(top = 16.dp)
                            .alpha(alpha)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(-45f)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Circle(
                    lineColor = lineColor,
                    fillColor = lineColor,
                    ratio = circleChartData.largeRatio(),
                    backgroundRes = circleChartData.largeCircle().backgroundRes,
                    isVisible = isHeartVisible,
                    onClick = {
                        isHeartVisible = isHeartVisible.not()
                        isBrokenHeartVisible = false
                    }
                )
                Circle(
                    lineColor = lineColor,
                    fillColor = fillColor,
                    ratio = circleChartData.smallRatio(),
                    backgroundRes = circleChartData.smallCircle().backgroundRes,
                    isVisible = isBrokenHeartVisible,
                    onClick = {
                        isBrokenHeartVisible = isBrokenHeartVisible.not()
                        isHeartVisible = false
                    }
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(top = 232.dp)
                    .clearAndSetSemantics { }
            ) {
                Text(
                    text = circleChartData.smallCircle().title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Row{
                    Text(
                        text = circleChartData.smallCircle().value.toInt().toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    val alpha by animateFloatAsState(
                        animationSpec = tween(durationMillis = 250),
                        targetValue = if (isBrokenHeartVisible) 1f else 0f
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_broken_heart),
                        contentDescription = null,
                        Modifier
                            .padding(top = 12.dp)
                            .alpha(alpha)
                    )
                }
            }
        }
    }
}

@Composable
fun Circle(
    lineColor: Color,
    fillColor: Color,
    ratio: Float,
    isVisible: Boolean,
    @DrawableRes backgroundRes: Int,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        animationSpec = tween(durationMillis = 250),
        targetValue = if (isVisible) 1f else 0f
    )

    Box(
        modifier = Modifier
            .size(170.dp * ratio)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = lineColor,
                shape = CircleShape
            )
            .background(fillColor)
            .clearAndSetSemantics {  }
            .clickable {
                onClick.invoke()
            }
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            modifier = Modifier.alpha(alpha)
        )
    }
}