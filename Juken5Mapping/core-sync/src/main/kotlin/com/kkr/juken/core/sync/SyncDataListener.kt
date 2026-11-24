package com.kkr.juken.core.sync

import com.kkr.juken.core.protocol.model.EcuIdentity
import com.kkr.juken.core.protocol.model.HistoryEntry
import com.kkr.juken.domain.model.tuning.MapType

/**
 * Observer for data extracted during the synchronization process.
 * Used to decouple SyncEngine from high-level repositories.
 */
interface SyncDataListener {
    suspend fun onEcuIdentityReceived(identity: EcuIdentity)
    suspend fun onGlobalVariablesReceived(variables: Map<String, String>)
    suspend fun onMapBlockReceived(type: MapType, coreIndex: Int, blockIndex: Int, values: List<Int>)
    suspend fun onHistoryReceived(blockIndex: Int, entries: List<HistoryEntry>)
    suspend fun onPatternReceived(blockIndex: Int, values: List<Int>)
}
