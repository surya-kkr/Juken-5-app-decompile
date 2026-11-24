package com.kkr.juken.feature.autotune.workspace.presentation.explorer.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.components.GlassIconButton

@Composable
fun RecommendationToolbar(
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlassIconButton(icon = Icons.Default.Sort, onClick = onSortClick)
        Spacer(modifier = Modifier.width(8.dp))
        GlassIconButton(icon = Icons.Default.FilterList, onClick = onFilterClick)
    }
}
