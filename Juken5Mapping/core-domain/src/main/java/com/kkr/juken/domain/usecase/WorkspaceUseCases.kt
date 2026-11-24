package com.kkr.juken.domain.usecase

import com.kkr.juken.domain.model.autotune.TuningRecommendation
import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import kotlinx.coroutines.flow.Flow

/**
 * UseCase for restoring the previous workspace layout from persistent storage.
 */
interface RestoreWorkspaceLayoutUseCase {
    suspend operator fun invoke(): WorkspaceLayout
}

/**
 * UseCase for persisting the current workspace layout.
 */
interface SaveWorkspaceLayoutUseCase {
    suspend operator fun invoke(layout: WorkspaceLayout)
}

/**
 * UseCase for loading intelligent tuning recommendations for a given project.
 */
interface LoadRecommendationsUseCase {
    suspend operator fun invoke(projectId: String): Flow<List<TuningRecommendation>>
}

/**
 * UseCase for accepting a specific tuning recommendation.
 */
interface AcceptRecommendationUseCase {
    suspend operator fun invoke(recommendationId: String)
}

/**
 * UseCase for rejecting a specific tuning recommendation.
 */
interface RejectRecommendationUseCase {
    suspend operator fun invoke(recommendationId: String)
}

/**
 * UseCase for generating a comprehensive technical tuning report.
 */
interface GenerateTuningReportUseCase {
    suspend operator fun invoke(projectId: String): String // Returns path or formatted content
}
