package com.kkr.juken.core.sync.internal

/**
 * Logic for mapping real-time engine telemetry to grid coordinates.
 * Supports Juken 5 standard 21x61 map topology.
 */
internal class ActiveCellTracker(
    private val rows: Int = 21,
    private val cols: Int = 61
) {
    // Standard Juken 5 RPM Breakpoints (21)
    private val rpmBreakpoints = IntArray(21) { it * 500 } // 0, 500, ..., 10000
    
    // Standard Juken 5 TPS Breakpoints (61)
    private val tpsBreakpoints = DoubleArray(61) { it * (100.0 / 60.0) } // 0, 1.66, ..., 100

    /**
     * Finds the primary active cell index based on current telemetry.
     */
    fun findActiveIndex(rpm: Int, tps: Double): Int {
        val r = findNearestIndex(rpm.toDouble(), rpmBreakpoints.map { it.toDouble() }.toDoubleArray())
        val c = findNearestIndex(tps, tpsBreakpoints)
        return r * cols + c
    }

    /**
     * Identifies the 4 cells used for bilinear interpolation.
     */
    fun findInterpolationIndices(rpm: Int, tps: Double): List<Int> {
        val r0 = findLowerIndex(rpm.toDouble(), rpmBreakpoints.map { it.toDouble() }.toDoubleArray())
        val c0 = findLowerIndex(tps, tpsBreakpoints)
        
        val r1 = (r0 + 1).coerceAtMost(rows - 1)
        val c1 = (c0 + 1).coerceAtMost(cols - 1)
        
        return listOf(
            r0 * cols + c0,
            r0 * cols + c1,
            r1 * cols + c0,
            r1 * cols + c1
        ).distinct()
    }

    private fun findNearestIndex(value: Double, breakpoints: DoubleArray): Int {
        var minDiff = Double.MAX_VALUE
        var nearest = 0
        breakpoints.forEachIndexed { index, b ->
            val diff = kotlin.math.abs(value - b)
            if (diff < minDiff) {
                minDiff = diff
                nearest = index
            }
        }
        return nearest
    }

    private fun findLowerIndex(value: Double, breakpoints: DoubleArray): Int {
        for (i in 0 until breakpoints.size - 1) {
            if (value >= breakpoints[i] && value < breakpoints[i + 1]) return i
        }
        return breakpoints.size - 1
    }
}
