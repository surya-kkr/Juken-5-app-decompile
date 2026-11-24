package com.kkr.juken.core.autotune.engine

import com.kkr.juken.domain.model.autotune.GroupType
import com.kkr.juken.domain.model.autotune.RecommendationGroup
import com.kkr.juken.domain.model.autotune.TuningRecommendation

/**
 * Hierarchically clusters recommendations for the Explorer tree.
 */
class GroupingEngine {

    fun groupByMapAndMode(items: List<TuningRecommendation>): RecommendationGroup {
        val mapGroups = items.groupBy { it.mapType }.map { (type, typeItems) ->
            val modeGroups = typeItems.groupBy { it.drivingMode }.map { (mode, modeItems) ->
                createGroup(mode, GroupType.DRIVING_MODE, modeItems)
            }
            createGroup(type.name, GroupType.MAP_TYPE, typeItems, modeGroups)
        }

        return createGroup("All Recommendations", GroupType.ROOT, items, mapGroups)
    }

    private fun createGroup(
        name: String, 
        type: GroupType, 
        items: List<TuningRecommendation>, 
        subGroups: List<RecommendationGroup> = emptyList()
    ): RecommendationGroup {
        return RecommendationGroup(
            name = name,
            type = type,
            recommendations = items,
            subGroups = subGroups,
            averageConfidence = if (items.isEmpty()) 0f else items.map { it.confidence }.average().toFloat(),
            totalHits = items.sumOf { it.hitCount }
        )
    }
}
