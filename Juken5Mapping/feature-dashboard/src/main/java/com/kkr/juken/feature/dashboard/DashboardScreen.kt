package com.kkr.juken.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkr.juken.core.ui.components.*
import com.kkr.juken.core.ui.layout.JukenAdaptiveScaffold
import com.kkr.juken.core.ui.theme.JukenTokens
import com.kkr.juken.feature.dashboard.presentation.sync.SyncOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        GlassBackground {
            Column(modifier = Modifier.fillMaxSize()) {
                GlassTopBar(
                    title = uiState.vehicle.modelName.uppercase(),
                    actions = {
                        GlassIconButton(
                            icon = Icons.Default.Settings,
                            onClick = { viewModel.onNavigateToSettings() }
                        )
                    }
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        GlassCard {
                            CircularGauge(
                                value = uiState.live.rpm.value,
                                min = uiState.live.rpm.min,
                                max = uiState.live.rpm.max,
                                label = uiState.live.rpm.label,
                                unit = uiState.live.rpm.unit,
                                warningThreshold = uiState.live.rpm.warningThreshold,
                                criticalThreshold = uiState.live.rpm.criticalThreshold
                            )
                        }
                    }
                    item {
                        GlassCard {
                            LinearGauge(
                                value = uiState.live.tps.value,
                                min = uiState.live.tps.min,
                                max = uiState.live.tps.max,
                                label = uiState.live.tps.label
                            )
                        }
                    }
                    item {
                        GlassCard {
                            CircularGauge(
                                value = uiState.live.afr.value,
                                min = uiState.live.afr.min,
                                max = uiState.live.afr.max,
                                label = uiState.live.afr.label,
                                unit = uiState.live.afr.unit
                            )
                        }
                    }
                }
            }
        }
        
        SyncOverlay(state = uiState.connection)
    }
}
