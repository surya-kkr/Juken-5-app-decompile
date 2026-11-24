package com.kkr.juken.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun GlassChip(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = JukenTokens.Colors.Primary
) {
    val shape = RoundedCornerShape(JukenTokens.Radius.Small)
    GlassSurface(
        modifier = modifier,
        shape = shape,
        blur = JukenTokens.Spacing.Small
    ) {
        Box(modifier = Modifier.padding(horizontal = JukenTokens.Spacing.Small, vertical = JukenTokens.Spacing.ExtraSmall)) {
            Text(
                text = text,
                color = color,
                fontSize = 12.sp
            )
        }
    }
}
