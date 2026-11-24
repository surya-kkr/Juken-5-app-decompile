package com.kkr.juken.feature.autotune.workspace.presentation.workspace.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.components.GlassCard
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun TimelinePanel(
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "SESSION TIMELINE",
                color = JukenTokens.Colors.Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "High-performance timeline and synchronized charts.",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 13.sp
            )
        }
    }
}
