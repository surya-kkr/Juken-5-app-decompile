package com.kkr.juken.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun GlassTopBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    GlassSurface(
        modifier = Modifier.fillMaxWidth().height(64.dp),
        shape = RectangleShape,
        blur = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = JukenTokens.Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = JukenTokens.Colors.Primary,
                fontWeight = FontWeight.Bold,
                fontSize = JukenTokens.Typography.TitleSize
            )
            Row { actions() }
        }
    }
}

@Composable
fun GlassNavigationBar(
    content: @Composable RowScope.() -> Unit
) {
    GlassSurface(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        shape = RectangleShape,
        blur = 15.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
