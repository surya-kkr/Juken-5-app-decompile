package com.kkr.juken.core.analysis

import kotlin.math.sqrt

/**
 * Standard statistical calculations for high-performance telemetry analysis.
 */
object StatisticsEngine {

    fun average(values: DoubleArray): Double = if (values.isEmpty()) 0.0 else values.average()

    fun standardDeviation(values: DoubleArray): Double {
        if (values.isEmpty()) return 0.0
        val avg = values.average()
        val variance = values.map { (it - avg) * (it - avg) }.average()
        return sqrt(variance)
    }

    fun median(values: DoubleArray): Double {
        if (values.isEmpty()) return 0.0
        val sorted = values.sortedArray()
        return if (sorted.size % 2 == 0) {
            (sorted[sorted.size / 2] + sorted[sorted.size / 2 - 1]) / 2.0
        } else {
            sorted[sorted.size / 2]
        }
    }
}
