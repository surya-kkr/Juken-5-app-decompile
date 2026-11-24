package com.kkr.juken.core.workspace.usecase

import com.kkr.juken.core.workspace.coordinator.RecommendationCoordinator
import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.usecase.AcceptRecommendationUseCase
import com.kkr.juken.domain.usecase.LoadRecommendationsUseCase
import com.kkr.juken.domain.usecase.RejectRecommendationUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LoadRecommendationsUseCaseImpl @Inject constructor(
    private val coordinator: RecommendationCoordinator
) : LoadRecommendationsUseCase {
    override suspend fun invoke(projectId: String): Flow<List<TuningRecommendation>> = 
        coordinator.getRecommendations(projectId)
}

internal class AcceptRecommendationUseCaseImpl @Inject constructor(
    private val coordinator: RecommendationCoordinator
) : AcceptRecommendationUseCase {
    override suspend fun invoke(recommendationId: String) = coordinator.accept(recommendationId)
}

internal class RejectRecommendationUseCaseImpl @Inject constructor(
    private val coordinator: RecommendationCoordinator
) : RejectRecommendationUseCase {
    override suspend fun invoke(recommendationId: String) = coordinator.reject(recommendationId)
}
