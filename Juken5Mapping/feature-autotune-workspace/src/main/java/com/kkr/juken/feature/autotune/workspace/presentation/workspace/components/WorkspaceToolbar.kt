package com.kkr.juken.feature.autotune.workspace.presentation.workspace.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewQuilt
import androidx.compose.material.icons.filled.ViewSidebar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.components.GlassIconButton
import com.kkr.juken.core.ui.components.GlassTopBar

@Composable
fun WorkspaceToolbar(
    onToggleLeft: () -> Unit,
    onToggleRight: () -> Unit,
    onToggleBottom: () -> Unit
) {
    GlassTopBar(
        title = "PROFESSIONAL TUNING WORKSPACE",
        actions = {
            GlassIconButton(icon = Icons.Default.ViewSidebar, onClick = onToggleLeft)
            Spacer(modifier = Modifier.width(8.dp))
            GlassIconButton(icon = Icons.Default.ViewQuilt, onClick = onToggleBottom)
            Spacer(modifier = Modifier.width(8.dp))
            GlassIconButton(icon = Icons.Default.ViewSidebar, onClick = onToggleRight)
        }
    )
}
