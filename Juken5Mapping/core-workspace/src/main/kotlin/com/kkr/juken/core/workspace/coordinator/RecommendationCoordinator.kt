package com.kkr.juken.core.workspace.coordinator

import com.kkr.juken.domain.model.autotune.RecommendationStatus
import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.repository.RecommendationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates recommendation-related business logic.
 */
@Singleton
class RecommendationCoordinator @Inject constructor(
    private val repository: RecommendationRepository
) {
    fun getRecommendations(projectId: String): Flow<List<TuningRecommendation>> {
        return repository.getRecommendations(projectId)
    }

    suspend fun accept(id: String) {
        repository.updateStatus(id, RecommendationStatus.ACCEPTED)
    }

    suspend fun reject(id: String) {
        repository.updateStatus(id, RecommendationStatus.REJECTED)
    }
}
