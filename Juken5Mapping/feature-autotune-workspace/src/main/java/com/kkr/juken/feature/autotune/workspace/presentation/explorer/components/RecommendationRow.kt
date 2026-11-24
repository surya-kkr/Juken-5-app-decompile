package com.kkr.juken.feature.autotune.workspace.presentation.explorer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.theme.JukenTokens
import com.kkr.juken.domain.model.autotune.TuningRecommendation

@Composable
fun RecommendationRow(
    item: TuningRecommendation,
    isSelected: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(if (isSelected) JukenTokens.Colors.Primary.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick(false) }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Cell ${item.cellIndex}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.reason ?: "Auto correction",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            val delta = item.recommendedValue - item.originalValue
            val color = if (delta >= 0) JukenTokens.Colors.Success else JukenTokens.Colors.Critical
            Text(
                text = (if (delta > 0) "+" else "") + delta.toString(),
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "${(item.confidence * 100).toInt()}% CONF",
                color = JukenTokens.Colors.Gray400,
                fontSize = 10.sp
            )
        }
    }
}
