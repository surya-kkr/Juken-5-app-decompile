package com.kkr.juken.feature.connection.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkr.juken.core.ui.components.GlassBackground
import com.kkr.juken.core.ui.theme.JukenTokens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    onNavigateToScanner: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                SplashEffect.NavigateToScanner -> onNavigateToScanner()
                SplashEffect.NavigateToPermissions -> onNavigateToPermissions()
            }
        }
    }

    GlassBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for Juken Logo
                Text(
                    "J5",
                    fontSize = 64.sp,
                    color = JukenTokens.Colors.Primary,
                    fontWeight = FontWeight.Black
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "JUKEN 5 MAPPING",
                color = Color.White,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            Text(
                "PRO EDITION",
                color = JukenTokens.Colors.Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
