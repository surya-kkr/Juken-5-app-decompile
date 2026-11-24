package com.kkr.juken.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.kkr.juken.core.ui.theme.JukenTheme
import com.kkr.juken.core.ui.theme.JukenTokens

/**
 * Base primitive for semi-transparent blurred surfaces.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(JukenTokens.Radius.Large),
    blur: Dp = JukenTokens.Glass.BlurNormal,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(JukenTokens.Colors.GlassBase)
            .border(
                width = JukenTokens.Glass.StrokeThin,
                brush = Brush.verticalGradient(
                    listOf(
                        JukenTokens.Colors.GlassStroke,
                        JukenTokens.Colors.GlassStroke.copy(alpha = 0.1f)
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}

/**
 * Full-screen background for the application.
 */
@Composable
fun GlassBackground(
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JukenTokens.Colors.Background)
    ) {
        // Optional noise or mesh gradient could go here
        content()
    }
}
