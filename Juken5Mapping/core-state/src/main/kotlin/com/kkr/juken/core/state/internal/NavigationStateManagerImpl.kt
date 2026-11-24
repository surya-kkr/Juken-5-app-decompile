package com.kkr.juken.core.state.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kkr.juken.domain.model.state.NavigationState
import com.kkr.juken.domain.state.NavigationStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NavigationStateManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) : NavigationStateManager {

    private val lastRouteKey = stringPreferencesKey("last_route")

    private val _memoryState = MutableStateFlow(NavigationState())
    override val state: StateFlow<NavigationState> = _memoryState.asStateFlow()

    override fun navigateTo(route: String) {
        _memoryState.value = _memoryState.value.copy(currentRoute = route)
        scope.launch {
            dataStore.edit { it[lastRouteKey] = route }
        }
    }

    override fun popBackstack() { }
}
