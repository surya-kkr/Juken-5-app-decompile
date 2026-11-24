package com.kkr.juken.feature.autotune.workspace.presentation.explorer.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.autotune.engine.FilterCriteria
import com.kkr.juken.core.ui.components.GlassCard
import com.kkr.juken.core.ui.components.GlassSlider
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun RecommendationFilterPanel(
    criteria: FilterCriteria,
    onCriteriaChange: (FilterCriteria) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "FILTERS",
                color = JukenTokens.Colors.Primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Minimum Confidence", color = Color.White, fontSize = 12.sp)
            GlassSlider(
                value = criteria.minConfidence,
                onValueChange = { onCriteriaChange(criteria.copy(minConfidence = it)) },
                valueRange = 0f..1f
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Max Fuel Change (%)", color = Color.White, fontSize = 12.sp)
            GlassSlider(
                value = criteria.maxFuelChangePercent,
                onValueChange = { onCriteriaChange(criteria.copy(maxFuelChangePercent = it)) },
                valueRange = 1f..50f
            )
        }
    }
}
