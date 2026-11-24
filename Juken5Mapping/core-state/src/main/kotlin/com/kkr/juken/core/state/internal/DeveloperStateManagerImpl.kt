package com.kkr.juken.core.state.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.kkr.juken.domain.model.state.DeveloperState
import com.kkr.juken.domain.state.DeveloperStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DeveloperStateManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) : DeveloperStateManager {

    private val inspectorKey = booleanPreferencesKey("dev_inspector")
    private val monitorKey = booleanPreferencesKey("dev_monitor")
    private val simulatorKey = booleanPreferencesKey("dev_simulator")

    override val state: StateFlow<DeveloperState> = dataStore.data
        .map { prefs ->
            DeveloperState(
                isPacketInspectorEnabled = prefs[inspectorKey] ?: false,
                isProtocolMonitorEnabled = prefs[monitorKey] ?: false,
                useSimulator = prefs[simulatorKey] ?: true
            )
        }.stateIn(scope, SharingStarted.WhileSubscribed(5000), DeveloperState())

    override fun togglePacketInspector(enabled: Boolean) {
        scope.launch {
            dataStore.edit { it[inspectorKey] = enabled }
        }
    }
}
