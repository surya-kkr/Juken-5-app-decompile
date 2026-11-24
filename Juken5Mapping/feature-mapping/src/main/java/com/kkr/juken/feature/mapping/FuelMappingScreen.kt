package com.kkr.juken.feature.mapping

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kkr.juken.domain.model.SelectionRange
import com.kkr.juken.feature.mapping.components.TuningCell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelMappingScreen(
    viewModel: FuelMappingViewModel = viewModel()
) {
    val map by viewModel.tuningMap.collectAsState()
    val selection by viewModel.selection.collectAsState()

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fuel Mapping") },
                actions = {
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo")
                    }
                    IconButton(onClick = { viewModel.redo() }) {
                        Icon(Icons.Default.Redo, contentDescription = "Redo")
                    }
                    IconButton(onClick = { viewModel.interpolate() }) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = "Interpolate")
                    }
                    IconButton(onClick = { viewModel.copy() }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                    }
                    IconButton(onClick = { viewModel.paste() }) {
                        Icon(Icons.Default.ContentPaste, contentDescription = "Paste")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .transformable(state = state)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(map.cols),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            ) {
                items(
                    count = map.cells.size,
                    key = { it }
                ) { index ->
                    val row = index / map.cols
                    val col = index % map.cols
                    val value = map.cells[index]
                    val isSelected = selection?.contains(row, col) == true
                    
                    TuningCell(
                        value = value,
                        isSelected = isSelected,
                        onClick = {
                            if (selection == null) {
                                viewModel.updateSelection(SelectionRange(row, col, row, col))
                            } else {
                                viewModel.updateSelection(selection?.copy(endRow = row, endCol = col))
                            }
                        }
                    )
                }
            }
        }
    }
}
