package com.kkr.juken.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = JukenTokens.Radius.Large.let { androidx.compose.foundation.shape.RoundedCornerShape(it) },
    contentPadding: Dp = JukenTokens.Spacing.Medium,
    content: @Composable () -> Unit
) {
    GlassSurface(
        modifier = modifier,
        shape = shape
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}
