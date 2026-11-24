package com.kkr.juken.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun CircularGauge(
    value: Float,
    min: Float,
    max: Float,
    label: String,
    unit: String,
    modifier: Modifier = Modifier,
    warningThreshold: Float? = null,
    criticalThreshold: Float? = null
) {
    val animatedValue = remember { Animatable(min) }
    LaunchedEffect(value) { animatedValue.animateTo(value, tween(300)) }
    
    val color by animateColorAsState(
        targetValue = when {
            criticalThreshold != null && value >= criticalThreshold -> JukenTokens.Colors.Critical
            warningThreshold != null && value >= warningThreshold -> JukenTokens.Colors.Warning
            else -> JukenTokens.Colors.Primary
        }, label = "Color"
    )

    Box(modifier = modifier.size(180.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweep = ((animatedValue.value - min) / (max - min)).coerceIn(0f, 1f) * 240f
            drawArc(
                color = JukenTokens.Colors.Gray800,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                style = Stroke(12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = 150f,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(12.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = JukenTokens.Colors.Gray400, fontSize = 14.sp)
            Text(value.toInt().toString(), color = color, fontSize = 42.sp, fontWeight = FontWeight.Black)
            Text(unit, color = JukenTokens.Colors.Gray400, fontSize = 12.sp)
        }
    }
}

@Composable
fun LinearGauge(
    value: Float,
    min: Float,
    max: Float,
    label: String,
    modifier: Modifier = Modifier
) {
    val animatedValue = remember { Animatable(min) }
    LaunchedEffect(value) { animatedValue.animateTo(value, tween(300)) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = JukenTokens.Colors.Gray400, fontSize = 12.sp)
            Text(value.toInt().toString(), color = JukenTokens.Colors.Primary, fontWeight = FontWeight.Bold)
        }
        Canvas(modifier = Modifier.fillMaxWidth().height(8.dp)) {
            val progress = ((animatedValue.value - min) / (max - min)).coerceIn(0f, 1f)
            drawLine(
                color = JukenTokens.Colors.Gray800,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )
            drawLine(
                color = JukenTokens.Colors.Primary,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width * progress, size.height / 2),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )
        }
    }
}
