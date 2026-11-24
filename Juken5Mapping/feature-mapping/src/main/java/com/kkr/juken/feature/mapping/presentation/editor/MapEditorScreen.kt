package com.kkr.juken.feature.mapping.presentation.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkr.juken.core.ui.components.*
import com.kkr.juken.domain.model.ui.TransferUiState
import com.kkr.juken.feature.mapping.presentation.editor.components.MapEditorToolbar
import com.kkr.juken.feature.mapping.presentation.editor.engine.RenderingEngine

@Composable
fun MapEditorScreen(
    viewModel: MapEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val realtimeState by viewModel.realtimeState.collectAsState()
    val grid = viewModel.grid
    val renderer = remember { RenderingEngine(grid) }

    GlassBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            MapEditorToolbar(uiState, viewModel::onAction)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                val cell = grid.hitTest(offset.x, offset.y)
                                viewModel.onAction(MapEditorAction.SelectCell(cell.first, cell.second))
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            grid.viewport.offsetX += pan.x
                            grid.viewport.offsetY += pan.y
                            grid.viewport.scale = (grid.viewport.scale * zoom).coerceIn(0.1f, 5f)
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    grid.onSizeChanged(size.width.toInt(), size.height.toInt())
                    renderer.draw(this, uiState.mapState, realtimeState)
                }
            }

            // Bottom Editing Panel
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentPadding = 16.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                     GlassSlider(
                        value = 0f, 
                        onValueChange = { viewModel.onAction(MapEditorAction.EditSelected(it.toInt())) }
                     )
                }
            }
        }

        // Read/Write Overlays
        TransferOverlay(uiState.writeState, viewModel::onAction)
    }
}

@Composable
private fun TransferOverlay(
    state: TransferUiState,
    onAction: (MapEditorAction) -> Unit
) {
    when (state) {
        is TransferUiState.InProgress -> {
            GlassTransferDialog(
                title = "Writing ECU",
                progress = state.progress / 100f,
                statusText = state.currentStep,
                metricsText = "Retry count: ${state.metrics.retryCount}"
            )
        }
        is TransferUiState.Verifying -> {
            GlassTransferDialog(
                title = "Verifying ECU",
                progress = state.progress / 100f,
                statusText = "Confirming data integrity..."
            )
        }
        is TransferUiState.Completed -> {
            GlassTransferResultDialog(
                title = "Write Success",
                message = "ECU updated successfully and verified.",
                isSuccess = true,
                onDismiss = { onAction(MapEditorAction.DismissTransfer) }
            )
        }
        is TransferUiState.Failed -> {
            GlassTransferResultDialog(
                title = "Write Failed",
                message = state.reason,
                isSuccess = false,
                onDismiss = { onAction(MapEditorAction.DismissTransfer) }
            )
        }
        else -> {}
    }
}
