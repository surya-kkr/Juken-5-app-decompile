package com.kkr.juken.feature.dashboard

import androidx.lifecycle.ViewModel
import com.kkr.juken.domain.model.ui.DashboardState
import com.kkr.juken.domain.repository.DashboardRepository
import com.kkr.juken.domain.state.ApplicationStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    private val appState: ApplicationStateManager
) : ViewModel() {

    val uiState: StateFlow<DashboardState> = repository.dashboardState

    fun onNavigateToSettings() {
        appState.navigation.navigateTo("settings")
    }
}
