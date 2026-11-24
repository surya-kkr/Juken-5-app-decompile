package com.kkr.juken.feature.mapping.presentation.editor.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.components.GlassIconButton
import com.kkr.juken.core.ui.components.GlassTopBar
import com.kkr.juken.feature.mapping.presentation.editor.MapEditorAction
import com.kkr.juken.feature.mapping.presentation.editor.MapEditorUiState

@Composable
fun MapEditorToolbar(
    uiState: MapEditorUiState,
    onAction: (MapEditorAction) -> Unit
) {
    GlassTopBar(
        title = uiState.mapType.name,
        actions = {
            GlassIconButton(
                icon = Icons.Default.Undo,
                onClick = { onAction(MapEditorAction.Undo) },
                enabled = uiState.mapState.canUndo
            )
            Spacer(modifier = Modifier.width(8.dp))
            GlassIconButton(
                icon = Icons.Default.Redo,
                onClick = { onAction(MapEditorAction.Redo) },
                enabled = uiState.mapState.canRedo
            )
            Spacer(modifier = Modifier.width(8.dp))
            GlassIconButton(
                icon = Icons.Default.AutoFixHigh,
                onClick = { onAction(MapEditorAction.Interpolate) }
            )
        }
    )
}
