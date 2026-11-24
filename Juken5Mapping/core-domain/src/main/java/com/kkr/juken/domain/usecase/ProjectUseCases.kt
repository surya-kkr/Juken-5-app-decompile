package com.kkr.juken.domain.usecase

import com.kkr.juken.domain.model.state.ProjectMetadata
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to create a new tuning project with vehicle metadata.
 */
interface CreateProjectUseCase {
    suspend operator fun invoke(name: String, vehicle: String): String
}

/**
 * UseCase to open an existing project and restore its workspace.
 */
interface OpenProjectUseCase {
    suspend operator fun invoke(projectId: String)
}

/**
 * UseCase to retrieve the list of all available projects.
 */
interface GetProjectsUseCase {
    operator fun invoke(): Flow<List<ProjectMetadata>>
}
