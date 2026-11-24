package com.kkr.juken.core.data.repository

import com.kkr.juken.core.data.db.dao.MapDao
import com.kkr.juken.core.data.db.entity.MapEntity
import com.kkr.juken.domain.model.tuning.*
import com.kkr.juken.domain.repository.MapRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepositoryImpl @Inject constructor(
    private val dao: MapDao
) : MapRepository {

    override fun getMap(projectId: String, type: MapType): Flow<TuningMap?> {
        return dao.getMap(projectId, type.name).map { entity ->
            entity?.let {
                val rows = 21 
                val cols = 61
                TuningMap(
                    type = type,
                    coreId = 1,
                    rows = rows,
                    cols = cols,
                    cells = it.data.mapIndexed { index, value ->
                        TuningCell(CellCoordinate(index / cols, index % cols), value.toDouble())
                    }
                )
            }
        }
    }

    override suspend fun saveMap(projectId: String, map: TuningMap) {
        dao.insertMap(
            MapEntity(
                projectId = projectId,
                type = map.type.name,
                data = map.cells.map { it.value.toInt() }.toIntArray(),
                lastModified = System.currentTimeMillis()
            )
        )
    }

    override suspend fun invalidateCache(projectId: String) {
        dao.deleteMapsForProject(projectId)
    }
}
