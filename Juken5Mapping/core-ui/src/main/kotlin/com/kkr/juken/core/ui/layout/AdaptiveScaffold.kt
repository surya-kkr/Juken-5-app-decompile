package com.kkr.juken.core.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kkr.juken.core.ui.components.GlassSurface
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun JukenAdaptiveScaffold(
    widthSizeClass: WindowWidthSizeClass,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    navigationRail: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        if (widthSizeClass != WindowWidthSizeClass.Compact) {
            navigationRail()
        }
        Scaffold(
            topBar = topBar,
            bottomBar = {
                if (widthSizeClass == WindowWidthSizeClass.Compact) {
                    bottomBar()
                }
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            content = content
        )
    }
}
