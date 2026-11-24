package com.kkr.juken.domain.repository

import com.kkr.juken.domain.model.ui.DashboardState
import kotlinx.coroutines.flow.StateFlow

interface DashboardRepository {
    val dashboardState: StateFlow<DashboardState>
}
