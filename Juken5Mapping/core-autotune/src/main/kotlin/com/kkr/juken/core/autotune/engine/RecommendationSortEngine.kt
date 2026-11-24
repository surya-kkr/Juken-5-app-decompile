package com.kkr.juken.core.autotune.engine

import com.kkr.juken.domain.model.autotune.TuningRecommendation

/**
 * High-performance sorting for large recommendation sets.
 */
class RecommendationSortEngine {

    enum class SortType {
        CONFIDENCE, HIT_COUNT, DELTA, RPM, TPS, RECENCY, STATUS
    }

    fun sort(
        items: List<TuningRecommendation>,
        type: SortType,
        ascending: Boolean = true
    ): List<TuningRecommendation> {
        val sorted = when (type) {
            SortType.CONFIDENCE -> items.sortedBy { it.confidence }
            SortType.HIT_COUNT -> items.sortedBy { it.hitCount }
            SortType.DELTA -> items.sortedBy { Math.abs(it.recommendedValue - it.originalValue) }
            SortType.RPM -> items.sortedBy { it.rpmRange.first }
            SortType.TPS -> items.sortedBy { it.tpsRange.start }
            SortType.RECENCY -> items.sortedBy { it.timestamp }
            SortType.STATUS -> items.sortedBy { it.status }
        }
        
        return if (ascending) sorted else sorted.reversed()
    }
}
