package com.kkr.juken.core.data.repository

import com.kkr.juken.domain.model.autotune.RecommendationStatus
import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.repository.RecommendationRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepositoryImpl @Inject constructor() : RecommendationRepository {
    private val _recommendations = MutableStateFlow<List<TuningRecommendation>>(emptyList())

    override fun getRecommendations(projectId: String): Flow<List<TuningRecommendation>> = _recommendations

    override suspend fun updateStatus(id: String, status: RecommendationStatus) {
        _recommendations.update { current ->
            current.map { if (it.id == id) it.copy(status = status) else it }
        }
    }
}
