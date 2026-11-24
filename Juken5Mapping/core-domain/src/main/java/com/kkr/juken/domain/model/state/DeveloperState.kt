package com.kkr.juken.domain.model.state

data class DeveloperState(
    val isPacketInspectorEnabled: Boolean = false,
    val isProtocolMonitorEnabled: Boolean = false,
    val isFpsOverlayEnabled: Boolean = false,
    val isRecompositionCounterEnabled: Boolean = false,
    val useSimulator: Boolean = true,
    val useReplay: Boolean = false,
    val isStressTestActive: Boolean = false,
    val isValidationModeEnabled: Boolean = false
)
