package com.example.timer_jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.timer_jetpack.ui.theme.Timer_JetpackTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Timer(
                    totalTime = 100L * 1000L,
                    inactiveBarColor = Color.DarkGray,
                    activeBaColor = Color(0xFF37B900),
                    handleColor = Color.Green,
                    modifier = Modifier.size(300.dp)
                )

            }

        }
    }
}

@Composable
fun Timer(
    totalTime: Long,
    inactiveBarColor: Color,
    activeBaColor: Color,
    handleColor: Color,
    initialValue: Float = 1f,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 10.dp
) {


    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableStateOf(initialValue)
    }
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    var isTimeRunning by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = currentTime, key2 = isTimeRunning) {
        if (currentTime > 0 && isTimeRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.onSizeChanged {
            size = it
        }
    ) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                -215f,
                250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = activeBaColor,
                -215f,
                250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250 * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r

            drawPoints(
                listOf(
                    Offset(center.x + a, center.y + b)
                ),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3).toPx(),
                cap = StrokeCap.Round
            )
        }

        Column() {

            Text(
                text = (currentTime / 1000).toString(),
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    if (currentTime <= 0) {
                        currentTime = totalTime
                        isTimeRunning = true
                    } else {
                        isTimeRunning = !isTimeRunning
                    }
                },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (!isTimeRunning || currentTime <= 0L) {
                        Color.Green
                    } else {
                        Color.Red
                    }
                )
            ) {
                Text(
                    text = if (isTimeRunning && currentTime >= 0L) "stop"
                    else if (!isTimeRunning && currentTime >= 0) "Start"
                    else "Restart"
                )
            }
        }
    }


}