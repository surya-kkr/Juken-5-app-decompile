package com.kkr.juken.core.workspace.layers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Data model for a map layer's visual state.
 */
data class LayerState(
    val id: String,
    val isVisible: Boolean = true,
    val opacity: Float = 1.0f,
    val blendMode: String = "Normal",
    val zIndex: Int = 0
)

/**
 * Manages the stack of layers visible on the Map Editor.
 */
class LayerStackManager {
    private val _stack = MutableStateFlow<List<LayerState>>(emptyList())
    val stack: StateFlow<List<LayerState>> = _stack.asStateFlow()

    fun addLayer(layer: LayerState) {
        _stack.update { (it + layer).sortedBy { l -> l.zIndex } }
    }

    fun removeLayer(layerId: String) {
        _stack.update { it.filter { l -> l.id != layerId } }
    }

    fun updateLayer(layerId: String, update: (LayerState) -> LayerState) {
        _stack.update { current ->
            current.map { if (it.id == layerId) update(it) else it }
        }
    }

    fun reorder(layerId: String, newZIndex: Int) {
        updateLayer(layerId) { it.copy(zIndex = newZIndex) }
        _stack.update { it.sortedBy { l -> l.zIndex } }
    }
}
