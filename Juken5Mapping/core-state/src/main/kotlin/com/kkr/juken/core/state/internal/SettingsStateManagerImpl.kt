package com.kkr.juken.core.state.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.kkr.juken.domain.model.state.AppTheme
import com.kkr.juken.domain.model.state.SettingsState
import com.kkr.juken.domain.model.state.TransportType
import com.kkr.juken.domain.state.SettingsStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingsStateManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) : SettingsStateManager {

    private val themeKey = stringPreferencesKey("theme")
    private val devModeKey = booleanPreferencesKey("dev_mode")
    private val transportKey = stringPreferencesKey("transport")
    private val autoConnectKey = booleanPreferencesKey("auto_connect")

    override val state: StateFlow<SettingsState> = dataStore.data
        .map { prefs ->
            SettingsState(
                theme = AppTheme.valueOf(prefs[themeKey] ?: AppTheme.DARK.name),
                isDeveloperModeEnabled = prefs[devModeKey] ?: false,
                selectedTransport = TransportType.valueOf(prefs[transportKey] ?: TransportType.SIMULATOR.name),
                autoConnect = prefs[autoConnectKey] ?: true
            )
        }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), SettingsState())

    override suspend fun updateTheme(theme: AppTheme) {
        dataStore.edit { it[themeKey] = theme.name }
    }

    override suspend fun setDeveloperMode(enabled: Boolean) {
        dataStore.edit { it[devModeKey] = enabled }
    }

    override suspend fun setTransport(type: TransportType) {
        dataStore.edit { it[transportKey] = type.name }
    }
}
