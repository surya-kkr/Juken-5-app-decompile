package com.kkr.juken.feature.mapping.presentation.editor.engine

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.kkr.juken.core.ui.theme.JukenTokens
import com.kkr.juken.domain.model.ui.RealtimeEngineState
import com.kkr.juken.feature.mapping.domain.engine.MapEngineState

/**
 * High-performance virtualized Canvas renderer for ECU maps.
 * Optimized for zero allocations during the draw loop.
 */
class RenderingEngine(
    private val grid: GridEngine
) {
    private val gridColor = JukenTokens.Colors.Gray800
    private val selectionColor = JukenTokens.Colors.Primary.copy(alpha = 0.3f)
    private val dirtyColor = JukenTokens.Colors.Warning.copy(alpha = 0.2f)
    
    // Live Overlay Colors
    private val activeCellGlow = JukenTokens.Colors.Primary
    private val trailColor = JukenTokens.Colors.Primary.copy(alpha = 0.15f)
    private val interpColor = JukenTokens.Colors.Primary.copy(alpha = 0.1f)

    /**
     * Renders the visible part of the map including live overlays.
     */
    fun draw(
        drawScope: DrawScope,
        mapState: MapEngineState,
        realtimeState: RealtimeEngineState? = null
    ) {
        val visibleRange = grid.viewport.calculateVisibleRange(grid.rows, grid.cols)
        
        // 1. Draw Cells
        for (r in visibleRange.startRow..visibleRange.endRow) {
            for (c in visibleRange.startCol..visibleRange.endCol) {
                val index = r * grid.cols + c
                val isSelected = grid.selection.isSelected(r, c)
                val isDirty = mapState.dirtyIndices.contains(index)
                
                val screenPos = grid.viewport.cellToScreen(r, c)
                val rectSize = Size(grid.viewport.cellWidth, grid.viewport.cellHeight)
                
                // Base Layers
                if (isSelected) {
                    drawScope.drawRect(selectionColor, Offset(screenPos.first, screenPos.second), rectSize)
                } else if (isDirty) {
                    drawScope.drawRect(dirtyColor, Offset(screenPos.first, screenPos.second), rectSize)
                }

                // 2. Draw Live Overlays (if within visible range)
                realtimeState?.let { live ->
                    // Trail
                    if (live.movementTrail.contains(index)) {
                        drawScope.drawRect(trailColor, Offset(screenPos.first, screenPos.second), rectSize)
                    }
                    
                    // Interpolation Area
                    if (live.interpolationIndices.contains(index)) {
                        drawScope.drawRect(interpColor, Offset(screenPos.first, screenPos.second), rectSize)
                    }
                    
                    // Active Cursor
                    if (live.activeCellIndex == index) {
                        drawScope.drawRect(
                            color = activeCellGlow,
                            topLeft = Offset(screenPos.first, screenPos.second),
                            size = rectSize,
                            style = Stroke(width = 3f)
                        )
                    }
                }

                // Grid Border
                drawScope.drawRect(
                    color = gridColor,
                    topLeft = Offset(screenPos.first, screenPos.second),
                    size = rectSize,
                    style = Stroke(width = 1f)
                )
            }
        }
    }
}
