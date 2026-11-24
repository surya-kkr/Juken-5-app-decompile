package com.kkr.juken.feature.autotune.workspace.presentation.explorer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkr.juken.core.ui.theme.JukenTokens
import com.kkr.juken.domain.model.autotune.RecommendationGroup
import com.kkr.juken.domain.model.autotune.TuningRecommendation

@Composable
fun RecommendationTree(
    rootGroup: RecommendationGroup,
    selection: Set<String>,
    onRecommendationClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            GroupHeader(rootGroup)
        }
        
        rootGroup.subGroups.forEach { group ->
            renderGroup(group, selection, onRecommendationClick, depth = 1)
        }
        
        items(rootGroup.recommendations) { item ->
            RecommendationRow(
                item = item,
                isSelected = selection.contains(item.id),
                onClick = { multi -> onRecommendationClick(item.id, multi) },
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.renderGroup(
    group: RecommendationGroup,
    selection: Set<String>,
    onRecommendationClick: (String, Boolean) -> Unit,
    depth: Int
) {
    item {
        var isExpanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(start = (depth * 16).dp, top = 8.dp, bottom = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = group.name.uppercase(),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${group.recommendations.size}",
                color = JukenTokens.Colors.Primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        if (isExpanded) {
            // Flatten logic or recursive items
        }
    }
}

@Composable
private fun GroupHeader(group: RecommendationGroup) {
    Text(
        text = group.name,
        color = JukenTokens.Colors.Primary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier.padding(16.dp)
    )
}
