package com.kkr.juken.feature.mapping.domain.engine.internal

import kotlin.math.roundToInt

/**
 * High-precision math engine for 2D map interpolation.
 */
internal class InterpolationEngine(private val storage: MapStorage) {

    /**
     * Performs Bilinear Interpolation within a rectangular range defined by 4 corners.
     */
    fun interpolate(
        startRow: Int, startCol: Int,
        endRow: Int, endCol: Int
    ): Map<Int, Int> {
        val minR = minOf(startRow, endRow)
        val maxR = maxOf(startRow, endRow)
        val minC = minOf(startCol, endCol)
        val maxC = maxOf(startCol, endCol)
        
        if (minR == maxR || minC == maxC) return emptyMap()

        val v00 = storage.getValue(minR, minC).toFloat()
        val v01 = storage.getValue(minR, maxC).toFloat()
        val v10 = storage.getValue(maxR, minC).toFloat()
        val v11 = storage.getValue(maxR, maxC).toFloat()

        val results = mutableMapOf<Int, Int>()
        val rowSpan = (maxR - minR).toFloat()
        val colSpan = (maxC - minC).toFloat()

        for (r in minR..maxR) {
            for (c in minC..maxC) {
                val tr = (r - minR) / rowSpan
                val tc = (c - minC) / colSpan
                
                val value = (1 - tr) * (1 - tc) * v00 +
                            (1 - tr) * tc * v01 +
                            tr * (1 - tc) * v10 +
                            tr * tc * v11
                            
                results[r * storage.cols + c] = value.roundToInt()
            }
        }
        return results
    }
}
