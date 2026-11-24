package com.kkr.juken.core.data.repository

import com.kkr.juken.core.data.db.dao.ProjectDao
import com.kkr.juken.core.data.db.entity.ProjectEntity
import com.kkr.juken.domain.model.state.ProjectMetadata
import com.kkr.juken.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val dao: ProjectDao
) : ProjectRepository {

    override fun getAllProjects(): Flow<List<ProjectMetadata>> {
        return dao.getAllProjects().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProject(id: String): ProjectMetadata? {
        return dao.getProjectById(id)?.toDomain()
    }

    override suspend fun saveProject(project: ProjectMetadata) {
        dao.insertProject(
            ProjectEntity(
                id = project.id,
                name = project.name,
                vehicleName = "Default", 
                ecuModel = "Juken 5",
                firmwareVersion = "1.0",
                lastModified = project.lastModified
            )
        )
    }

    override suspend fun deleteProject(id: String) {
        val entity = dao.getProjectById(id)
        if (entity != null) {
            dao.deleteProject(entity)
        }
    }

    private fun ProjectEntity.toDomain() = ProjectMetadata(id, name, lastModified)
}
