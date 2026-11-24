package com.kkr.juken.feature.autotune.workspace.presentation.inspector

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.components.GlassCard
import com.kkr.juken.core.ui.theme.JukenTokens
import com.kkr.juken.domain.model.autotune.TuningRecommendation

@Composable
fun RecommendationInspector(
    recommendation: TuningRecommendation?,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.fillMaxSize()) {
        if (recommendation == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Select a recommendation to inspect", color = Color.White.copy(alpha = 0.4f))
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "CELL ${recommendation.cellIndex}",
                    color = JukenTokens.Colors.Primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                DetailItem("Map Type", recommendation.mapType.name)
                DetailItem("Driving Mode", recommendation.drivingMode)
                DetailItem("Original Value", recommendation.originalValue.toString())
                DetailItem("Recommended", recommendation.recommendedValue.toString(), color = JukenTokens.Colors.Success)
                
                val delta = recommendation.recommendedValue - recommendation.originalValue
                DetailItem("Difference", (if (delta > 0) "+" else "") + delta.toString(), color = if (delta >= 0) JukenTokens.Colors.Success else JukenTokens.Colors.Critical)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("METRICS", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                DetailItem("Confidence", "${(recommendation.confidence * 100).toInt()}%")
                DetailItem("Hit Count", recommendation.hitCount.toString())
                DetailItem("Avg AFR", "%.2f".format(recommendation.averageAfr))
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String, color: Color = Color.White) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}
