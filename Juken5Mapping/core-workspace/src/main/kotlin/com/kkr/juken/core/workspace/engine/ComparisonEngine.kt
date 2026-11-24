package com.kkr.juken.core.workspace.engine

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the multi-stage comparison states for ECU maps.
 */
class ComparisonEngine {
    enum class Mode {
        ORIGINAL,
        CURRENT,
        RECOMMENDATION,
        PREVIEW,
        VERIFIED
    }

    private val _mode = MutableStateFlow(Mode.CURRENT)
    val mode: StateFlow<Mode> = _mode.asStateFlow()

    fun setMode(mode: Mode) {
        _mode.value = mode
    }
}
