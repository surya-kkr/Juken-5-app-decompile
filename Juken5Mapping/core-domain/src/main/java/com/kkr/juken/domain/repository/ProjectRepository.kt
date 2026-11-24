package com.kkr.juken.domain.repository

import com.kkr.juken.domain.model.state.ProjectMetadata
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<List<ProjectMetadata>>
    suspend fun getProject(id: String): ProjectMetadata?
    suspend fun saveProject(project: ProjectMetadata)
    suspend fun deleteProject(id: String)
}
