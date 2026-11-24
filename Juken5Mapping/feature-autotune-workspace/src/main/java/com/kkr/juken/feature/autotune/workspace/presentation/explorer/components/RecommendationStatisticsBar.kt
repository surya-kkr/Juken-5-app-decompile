package com.kkr.juken.feature.autotune.workspace.presentation.explorer.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun RecommendationStatisticsBar(
    total: Int,
    filtered: Int,
    accepted: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem(label = "TOTAL", value = total.toString())
        StatItem(label = "FILTERED", value = filtered.toString())
        StatItem(label = "ACCEPTED", value = accepted.toString(), color = JukenTokens.Colors.Success)
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color = Color.White) {
    Column {
        Text(text = label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
        Text(text = value, color = color, fontSize = 16.sp, fontWeight = FontWeight.Black)
    }
}
