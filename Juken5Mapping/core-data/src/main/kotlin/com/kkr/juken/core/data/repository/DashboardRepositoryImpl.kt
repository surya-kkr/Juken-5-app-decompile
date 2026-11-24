package com.kkr.juken.core.data.repository

import com.kkr.juken.domain.model.ui.*
import com.kkr.juken.domain.repository.DashboardRepository
import com.kkr.juken.domain.repository.EcuRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val ecuRepository: EcuRepository,
    private val scope: CoroutineScope
) : DashboardRepository {

    override val dashboardState: StateFlow<DashboardState> = combine(
        ecuRepository.connectionState,
        ecuRepository.realtimeEngineState
    ) { conn, engine ->
        DashboardState(
            connection = conn,
            vehicle = VehicleUiState(), // This should come from ProjectRepository
            live = LiveState(
                rpm = GaugeState(value = engine.rpm.toFloat(), min = 0f, max = 12000f),
                tps = GaugeState(value = engine.tps.toFloat(), min = 0f, max = 100f),
                battery = GaugeState(value = engine.battery.toFloat(), min = 0f, max = 20f),
                temperature = GaugeState(value = engine.temperature.toFloat(), min = 0f, max = 150f)
            )
        )
    }.stateIn(scope, SharingStarted.WhileSubscribed(), DashboardState())
}
