package com.kkr.juken.feature.mapping.domain.engine.internal

import kotlin.math.roundToInt

/**
 * Perform vectorized math operations on map ranges.
 */
internal class BatchOperationEngine(private val storage: MapStorage) {

    fun add(indices: Set<Int>, value: Int): Map<Int, Int> {
        return indices.associateWith { (storage.getValue(it) + value) }
    }

    fun multiply(indices: Set<Int>, factor: Float): Map<Int, Int> {
        return indices.associateWith { (storage.getValue(it) * factor).roundToInt() }
    }

    fun fill(indices: Set<Int>, value: Int): Map<Int, Int> {
        return indices.associateWith { value }
    }

    fun clamp(indices: Set<Int>, min: Int, max: Int): Map<Int, Int> {
        return indices.associateWith { storage.getValue(it).coerceIn(min, max) }
    }
}
