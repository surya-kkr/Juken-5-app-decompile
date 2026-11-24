package com.kkr.juken.core.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun ResponsiveGrid(
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val columns = when {
            maxWidth < 600.dp -> 1
            maxWidth < 900.dp -> 2
            else -> 4
        }
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            horizontalArrangement = Arrangement.spacedBy(JukenTokens.Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(JukenTokens.Spacing.Medium),
            content = content
        )
    }
}
