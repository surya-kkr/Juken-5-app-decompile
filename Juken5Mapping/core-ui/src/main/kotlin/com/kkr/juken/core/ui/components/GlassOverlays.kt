package com.kkr.juken.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun GlassLoadingOverlay(
    visible: Boolean,
    message: String
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            GlassCard(modifier = Modifier.width(280.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(JukenTokens.Spacing.Large)
                ) {
                    CircularProgressIndicator(color = JukenTokens.Colors.Primary)
                    Spacer(modifier = Modifier.height(JukenTokens.Spacing.Medium))
                    Text(
                        text = message,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun GlassDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    confirmText: String = "OK"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(modifier = Modifier.width(320.dp)) {
            Column(modifier = Modifier.padding(JukenTokens.Spacing.Large)) {
                Text(
                    text = title,
                    color = JukenTokens.Colors.Primary,
                    fontSize = JukenTokens.Typography.TitleSize,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(JukenTokens.Spacing.Small))
                Text(
                    text = message,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(JukenTokens.Spacing.Large))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    GlassButton(text = confirmText, onClick = onDismiss)
                }
            }
        }
    }
}
