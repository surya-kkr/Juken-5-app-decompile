package com.kkr.juken.feature.autotune.workspace.presentation.workspace.components

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
fun WorkspaceDivider(
    onResize: (Float) -> Unit,
    isHorizontal: Boolean = false,
    modifier: Modifier = Modifier
) {
    var parentSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .onSizeChanged { parentSize = it }
            .then(if (isHorizontal) Modifier.fillMaxWidth().height(4.dp) else Modifier.fillMaxHeight().width(4.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    val delta = if (isHorizontal) {
                        if (parentSize.height > 0) dragAmount.y / parentSize.height else 0f
                    } else {
                        if (parentSize.width > 0) dragAmount.x / parentSize.width else 0f
                    }
                    onResize(delta)
                }
            }
    )
}
