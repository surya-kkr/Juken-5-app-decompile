package com.kkr.juken.domain.repository

import com.kkr.juken.domain.model.tuning.MapType
import com.kkr.juken.domain.model.tuning.TuningMap
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun getMap(projectId: String, type: MapType): Flow<TuningMap?>
    suspend fun saveMap(projectId: String, map: TuningMap)
    suspend fun invalidateCache(projectId: String)
}
