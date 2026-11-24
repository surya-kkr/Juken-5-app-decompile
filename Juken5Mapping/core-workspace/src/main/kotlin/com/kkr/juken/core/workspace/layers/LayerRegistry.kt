package com.kkr.juken.core.workspace.layers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Registry for all available map layers in the application.
 * Supports dynamic registration of future analysis layers.
 */
class LayerRegistry {
    private val _availableLayers = MutableStateFlow<Set<String>>(
        setOf("ORIGINAL", "RECOMMENDED", "DIFFERENCE", "CONFIDENCE", "AFR_ERROR", "KNOCK", "TEMPERATURE", "CELL_USAGE")
    )
    val availableLayers: StateFlow<Set<String>> = _availableLayers.asStateFlow()

    fun registerLayer(id: String) {
        _availableLayers.update { it + id }
    }
}
