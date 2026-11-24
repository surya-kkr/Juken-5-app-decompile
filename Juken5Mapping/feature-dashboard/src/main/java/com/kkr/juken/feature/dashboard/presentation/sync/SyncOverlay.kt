package com.kkr.juken.feature.dashboard.presentation.sync

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.components.GlassCard
import com.kkr.juken.core.ui.components.GlassProgressIndicator
import com.kkr.juken.core.ui.theme.JukenTokens
import com.kkr.juken.domain.model.ui.ConnectionUiState

@Composable
fun SyncOverlay(
    state: ConnectionUiState
) {
    AnimatedVisibility(
        visible = state is ConnectionUiState.Synchronizing || state is ConnectionUiState.Connecting,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            GlassCard(modifier = Modifier.padding(32.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        if (state is ConnectionUiState.Connecting) "ESTABLISHING LINK" else "SYNCHRONIZING ECU",
                        color = JukenTokens.Colors.Primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (state is ConnectionUiState.Synchronizing) {
                        GlassProgressIndicator(
                            progress = state.progress / 100f,
                            label = state.step.uppercase()
                        )
                    } else {
                        CircularProgressIndicator(color = JukenTokens.Colors.Primary)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        "DO NOT DISCONNECT DEVICE",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
