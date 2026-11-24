package com.kkr.juken.feature.autotune.workspace.presentation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun ResizableWorkspace(
    leftPanel: @Composable BoxScope.() -> Unit,
    centerPanel: @Composable BoxScope.() -> Unit,
    rightPanel: @Composable BoxScope.() -> Unit,
    bottomPanel: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    initialLeftSplit: Float = 0.2f,
    initialRightSplit: Float = 0.8f,
    initialBottomSplit: Float = 0.7f,
    onSplitChange: (left: Float, right: Float, bottom: Float) -> Unit = { _, _, _ -> }
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var leftSplit by remember { mutableFloatStateOf(initialLeftSplit) }
    var rightSplit by remember { mutableFloatStateOf(initialRightSplit) }
    var bottomSplit by remember { mutableFloatStateOf(initialBottomSplit) }

    Column(modifier = modifier.fillMaxSize().onSizeChanged { size = it }) {
        Row(modifier = Modifier.weight(bottomSplit)) {
            // Left Panel
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(leftSplit)) {
                leftPanel()
            }
            
            // Vertical Divider
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(Color.White.copy(alpha = 0.1f))
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            if (size.width > 0) {
                                leftSplit = (leftSplit + dragAmount.x / size.width).coerceIn(0.1f, 0.4f)
                                onSplitChange(leftSplit, rightSplit, bottomSplit)
                            }
                        }
                    }
            )

            // Center Panel
            Box(modifier = Modifier.fillMaxHeight().weight(1f)) {
                centerPanel()
            }

            // Vertical Divider
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(Color.White.copy(alpha = 0.1f))
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            if (size.width > 0) {
                                rightSplit = (rightSplit + dragAmount.x / size.width).coerceIn(0.6f, 0.9f)
                                onSplitChange(leftSplit, rightSplit, bottomSplit)
                            }
                        }
                    }
            )

            // Right Panel
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(1f - rightSplit)) {
                rightPanel()
            }
        }

        // Horizontal Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.White.copy(alpha = 0.1f))
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        if (size.height > 0) {
                            bottomSplit = (bottomSplit + dragAmount.y / size.height).coerceIn(0.5f, 0.9f)
                            onSplitChange(leftSplit, rightSplit, bottomSplit)
                        }
                    }
                }
        )

        // Bottom Panel
        Box(modifier = Modifier.fillMaxWidth().weight(1f - bottomSplit)) {
            bottomPanel()
        }
    }
}
