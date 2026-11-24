package com.kkr.juken.core.charts.model

import androidx.compose.ui.graphics.Color

/**
 * Metadata for a single data series in the chart.
 */
data class ChartSeries(
    val name: String,
    val color: Color,
    val data: List<Float>, // Normalized or raw values
    val min: Float,
    val max: Float,
    val unit: String
)

/**
 * Display configuration for the chart engine.
 */
data class ChartConfig(
    val showGrid: Boolean = true,
    val showCursor: Boolean = true,
    val backgroundColor: Color = Color.Transparent,
    val synchronizedCursorPosition: Float? = null // 0.0 to 1.0 (session timeline)
)
