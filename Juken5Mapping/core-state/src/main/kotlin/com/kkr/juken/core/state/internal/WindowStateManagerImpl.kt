package com.kkr.juken.core.state.internal

import com.kkr.juken.domain.model.state.Orientation
import com.kkr.juken.domain.model.state.WindowSizeClass
import com.kkr.juken.domain.model.state.WindowState
import com.kkr.juken.domain.state.WindowStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class WindowStateManagerImpl @Inject constructor() : WindowStateManager {
    private val _state = MutableStateFlow(WindowState())
    override val state: StateFlow<WindowState> = _state.asStateFlow()

    override fun updateWindowSize(width: Int, height: Int, density: Float) {
        val sizeClass = when {
            width < 600 -> WindowSizeClass.COMPACT
            width < 840 -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }
        val orientation = if (width > height) Orientation.LANDSCAPE else Orientation.PORTRAIT
        
        _state.value = WindowState(
            sizeClass = sizeClass,
            orientation = orientation,
            screenWidthDp = width,
            screenHeightDp = height,
            displayDensity = density
        )
    }
}
