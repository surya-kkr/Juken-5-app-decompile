package com.kkr.juken.core.analysis

/**
 * Generic engine for aggregating frame data onto a 2D ECU map grid.
 */
class HeatmapEngine<T>(
    val rows: Int = 21,
    val cols: Int = 61
) {
    private val cellData = Array(rows * cols) { mutableListOf<T>() }

    /**
     * Records a data point for a specific cell index.
     */
    fun record(index: Int, data: T) {
        if (index in cellData.indices) {
            cellData[index].add(data)
        }
    }

    /**
     * Calculates the heatmap based on an aggregator function.
     */
    fun <R> compute(aggregator: (List<T>) -> R): List<R?> {
        return cellData.map { list ->
            if (list.isEmpty()) null else aggregator(list)
        }
    }

    fun clear() {
        cellData.forEach { it.clear() }
    }
}
