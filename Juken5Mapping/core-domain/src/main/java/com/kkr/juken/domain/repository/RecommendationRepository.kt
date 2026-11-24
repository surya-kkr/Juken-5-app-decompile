package com.kkr.juken.domain.repository

import com.kkr.juken.domain.model.autotune.TuningRecommendation
import kotlinx.coroutines.flow.Flow

/**
 * High-performance store for intelligent tuning recommendations.
 */
interface RecommendationRepository {
    fun getRecommendations(projectId: String): Flow<List<TuningRecommendation>>
    suspend fun updateStatus(id: String, status: com.kkr.juken.domain.model.autotune.RecommendationStatus)
}
