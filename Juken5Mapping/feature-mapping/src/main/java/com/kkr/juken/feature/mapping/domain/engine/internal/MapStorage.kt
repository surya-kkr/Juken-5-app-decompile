package com.kkr.juken.feature.mapping.domain.engine.internal

/**
 * High-performance contiguous memory storage for ECU maps.
 * Uses [IntArray] for O(1) access and low garbage collection overhead.
 */
internal class MapStorage(
    val rows: Int,
    val cols: Int,
    initialData: IntArray? = null
) {
    private val data = IntArray(rows * cols) { initialData?.getOrNull(it) ?: 0 }

    val size: Int get() = data.size

    fun getValue(row: Int, col: Int): Int {
        return data[row * cols + col]
    }

    fun setValue(row: Int, col: Int, value: Int) {
        data[row * cols + col] = value
    }

    fun getValue(index: Int): Int {
        return data[index]
    }

    fun setValue(index: Int, value: Int) {
        data[index] = value
    }

    /**
     * Returns a copy of the raw data.
     */
    fun snapshot(): IntArray = data.copyOf()

    /**
     * Batch update from a raw array.
     */
    fun restore(newData: IntArray) {
        require(newData.size == data.size)
        newData.copyInto(data)
    }
}
