package com.kkr.juken.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.theme.JukenTokens

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun GlassSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = JukenTokens.Colors.Primary,
                activeTrackColor = JukenTokens.Colors.Primary,
                inactiveTrackColor = Color.White.copy(alpha = 0.1f)
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(JukenTokens.Colors.Primary, CircleShape)
                )
            }
        )
    }
}
