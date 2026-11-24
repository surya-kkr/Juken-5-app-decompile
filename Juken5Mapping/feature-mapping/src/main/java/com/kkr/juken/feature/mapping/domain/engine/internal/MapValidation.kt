package com.kkr.juken.feature.mapping.domain.engine.internal

/**
 * Validates map values against physical hardware constraints.
 */
internal class ValidationEngine {
    
    fun validateCell(value: Int, min: Int, max: Int): Boolean {
        return value in min..max
    }

    fun findInvalidIndices(data: IntArray, min: Int, max: Int): Set<Int> {
        val invalid = mutableSetOf<Int>()
        data.forEachIndexed { index, value ->
            if (value < min || value > max) invalid.add(index)
        }
        return invalid
    }
}

/**
 * High-performance diffing between map states.
 */
internal class DifferenceEngine {

    fun diff(original: IntArray, current: IntArray): Set<Int> {
        require(original.size == current.size)
        val changed = mutableSetOf<Int>()
        for (i in original.indices) {
            if (original[i] != current[i]) changed.add(i)
        }
        return changed
    }
}
