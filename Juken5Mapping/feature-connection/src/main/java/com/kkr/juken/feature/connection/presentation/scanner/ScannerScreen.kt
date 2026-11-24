package com.kkr.juken.feature.connection.presentation.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkr.juken.core.ui.components.GlassBackground
import com.kkr.juken.core.ui.components.GlassButton
import com.kkr.juken.core.ui.components.GlassIconButton
import com.kkr.juken.core.ui.components.GlassTopBar
import com.kkr.juken.core.ui.theme.JukenTokens
import com.kkr.juken.feature.connection.presentation.scanner.components.DeviceCard

@Composable
fun ScannerScreen(
    onDeviceSelected: () -> Unit,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startScan()
    }

    GlassBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            GlassTopBar(
                title = "DISCOVER ECU",
                actions = {
                    GlassIconButton(
                        icon = Icons.Default.Refresh,
                        onClick = { viewModel.startScan() },
                        enabled = !uiState.isScanning
                    )
                }
            )

            if (uiState.isScanning) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(2.dp),
                    color = JukenTokens.Colors.Primary,
                    trackColor = Color.Transparent
                )
            } else {
                Spacer(modifier = Modifier.height(2.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.pairedDevices.isNotEmpty()) {
                    Text(
                        "PAIRED DEVICES",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(modifier = Modifier.heightIn(max = 340.dp)) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.pairedDevices) { device ->
                                DeviceCard(
                                    device = device,
                                    onClick = {
                                        viewModel.onDeviceSelected(device)
                                        onDeviceSelected()
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    "AVAILABLE DEVICES",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(modifier = Modifier.heightIn(max = 340.dp)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.availableDevices) { device ->
                            DeviceCard(
                                device = device,
                                onClick = {
                                    viewModel.onDeviceSelected(device)
                                    onDeviceSelected()
                                }
                            )
                        }
                    }
                }

                if (uiState.pairedDevices.isEmpty() && uiState.availableDevices.isEmpty() && !uiState.isScanning) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No devices found", color = Color.White.copy(alpha = 0.4f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                GlassButton(
                    text = "ENTER DEMO MODE",
                    onClick = {
                        viewModel.startDemoMode()
                        onDeviceSelected()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
