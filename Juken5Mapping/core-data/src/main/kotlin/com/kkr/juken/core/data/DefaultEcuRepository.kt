package com.kkr.juken.core.data

import com.kkr.juken.core.data.db.dao.MapDao
import com.kkr.juken.core.data.db.dao.ProjectDao
import com.kkr.juken.core.data.db.entity.MapEntity
import com.kkr.juken.core.protocol.model.*
import com.kkr.juken.core.sync.SyncDataListener
import com.kkr.juken.domain.model.tuning.MapType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultEcuRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val mapDao: MapDao
) : SyncDataListener {

    override suspend fun onEcuIdentityReceived(identity: EcuIdentity) { }

    override suspend fun onGlobalVariablesReceived(variables: Map<String, String>) { }

    override suspend fun onMapBlockReceived(type: MapType, coreIndex: Int, blockIndex: Int, values: List<Int>) {
        mapDao.insertMap(MapEntity(
            projectId = "current",
            type = type.name,
            data = values.toIntArray(),
            lastModified = System.currentTimeMillis()
        ))
    }

    override suspend fun onHistoryReceived(blockIndex: Int, entries: List<HistoryEntry>) { }

    override suspend fun onPatternReceived(blockIndex: Int, values: List<Int>) { }
}
