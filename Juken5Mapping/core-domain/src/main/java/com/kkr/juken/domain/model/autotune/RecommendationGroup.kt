package com.kkr.juken.domain.model.autotune

/**
 * A hierarchical cluster of recommendations for the Explorer tree.
 */
data class RecommendationGroup(
    val name: String,
    val type: GroupType,
    val recommendations: List<TuningRecommendation> = emptyList(),
    val subGroups: List<RecommendationGroup> = emptyList(),
    val isExpanded: Boolean = false,
    
    // Aggregated Stats
    val averageConfidence: Float = 0f,
    val totalHits: Int = 0,
    val maxCorrectionPercent: Float = 0f,
    val coveragePercent: Float = 0f
)

enum class GroupType {
    ROOT, MAP_TYPE, DRIVING_MODE, CONFIDENCE_LEVEL, REGION
}
