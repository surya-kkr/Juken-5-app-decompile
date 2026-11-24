package com.kkr.juken.domain.model.state

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }
enum class Orientation { PORTRAIT, LANDSCAPE }

data class WindowState(
    val sizeClass: WindowSizeClass = WindowSizeClass.COMPACT,
    val orientation: Orientation = Orientation.PORTRAIT,
    val screenWidthDp: Int = 0,
    val screenHeightDp: Int = 0,
    val displayDensity: Float = 1.0f
)
