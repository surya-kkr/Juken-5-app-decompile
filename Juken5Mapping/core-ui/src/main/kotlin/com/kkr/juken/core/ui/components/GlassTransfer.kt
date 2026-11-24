package com.kkr.juken.core.ui.components

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
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun GlassTransferDialog(
    title: String,
    progress: Float,
    statusText: String,
    metricsText: String? = null,
    onCancel: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(modifier = Modifier.width(320.dp)) {
            Column(
                modifier = Modifier.padding(JukenTokens.Spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title.uppercase(),
                    color = JukenTokens.Colors.Primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                GlassProgressIndicator(
                    progress = progress,
                    label = statusText.uppercase()
                )
                
                if (metricsText != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = metricsText,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }
                
                if (onCancel != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    GlassButton(text = "CANCEL", onClick = onCancel)
                }
            }
        }
    }
}

@Composable
fun GlassTransferResultDialog(
    title: String,
    message: String,
    isSuccess: Boolean,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(modifier = Modifier.width(300.dp)) {
            Column(
                modifier = Modifier.padding(JukenTokens.Spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title.uppercase(),
                    color = if (isSuccess) JukenTokens.Colors.Success else JukenTokens.Colors.Critical,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = message,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                GlassButton(text = "CONTINUE", onClick = onDismiss)
            }
        }
    }
}
