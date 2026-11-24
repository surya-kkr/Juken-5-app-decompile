package com.kkr.juken.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(JukenTokens.Radius.Medium)
    GlassSurface(
        modifier = modifier
            .clip(shape)
            .clickable(enabled = enabled, onClick = onClick),
        shape = shape
    ) {
        Box(
            modifier = Modifier.padding(horizontal = JukenTokens.Spacing.Large, vertical = JukenTokens.Spacing.Small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (enabled) JukenTokens.Colors.Primary else JukenTokens.Colors.Gray400,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
