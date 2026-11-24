package com.kkr.juken.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun GlassDrawer(
    content: @Composable ColumnScope.() -> Unit
) {
    GlassSurface(
        modifier = Modifier.fillMaxHeight().width(300.dp),
        shape = RectangleShape,
        blur = 25.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(JukenTokens.Spacing.Medium),
            content = content
        )
    }
}
