package com.kkr.juken.feature.autotune.workspace.presentation.workspace.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kkr.juken.domain.model.workspace.WorkspaceLayout

@Composable
fun WorkspaceScaffold(
    layout: WorkspaceLayout,
    onResizeLeft: (Float) -> Unit,
    onResizeRight: (Float) -> Unit,
    onResizeBottom: (Float) -> Unit,
    leftPanel: @Composable BoxScope.() -> Unit,
    centerPanel: @Composable BoxScope.() -> Unit,
    rightPanel: @Composable BoxScope.() -> Unit,
    bottomPanel: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f - if (layout.isBottomPanelCollapsed) 0f else layout.bottomPanelHeightFraction)) {
            // Left Panel
            AnimatedVisibility(
                visible = !layout.isLeftPanelCollapsed,
                modifier = Modifier.fillMaxHeight().fillMaxWidth(layout.leftPanelWidthFraction)
            ) {
                Box(modifier = Modifier.fillMaxSize()) { leftPanel() }
            }

            if (!layout.isLeftPanelCollapsed) {
                WorkspaceDivider(onResize = onResizeLeft)
            }

            // Center Panel
            Box(modifier = Modifier.fillMaxHeight().weight(1f)) {
                centerPanel()
            }

            if (!layout.isRightPanelCollapsed) {
                WorkspaceDivider(onResize = onResizeRight)
            }

            // Right Panel
            AnimatedVisibility(
                visible = !layout.isRightPanelCollapsed,
                modifier = Modifier.fillMaxHeight().fillMaxWidth(layout.rightPanelWidthFraction)
            ) {
                Box(modifier = Modifier.fillMaxSize()) { rightPanel() }
            }
        }

        if (!layout.isBottomPanelCollapsed) {
            WorkspaceDivider(onResize = onResizeBottom, isHorizontal = true)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(layout.bottomPanelHeightFraction)
            ) {
                bottomPanel()
            }
        }
    }
}
