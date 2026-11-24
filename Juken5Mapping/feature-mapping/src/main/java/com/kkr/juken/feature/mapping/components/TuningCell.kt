package com.kkr.juken.feature.mapping.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TuningCell(
    value: Double,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isSelected -> Color.Cyan.copy(alpha = 0.5f)
        value > 0 -> Color.Red.copy(alpha = (value / 100f).coerceIn(0.1, 0.8).toFloat())
        value < 0 -> Color.Blue.copy(alpha = ((-value) / 100f).coerceIn(0.1, 0.8).toFloat())
        else -> MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = modifier
            .aspectRatio(1.5f)
            .background(backgroundColor)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "%.1f".format(value),
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
