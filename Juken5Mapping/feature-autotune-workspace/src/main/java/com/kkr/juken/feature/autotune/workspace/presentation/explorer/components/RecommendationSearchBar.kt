package com.kkr.juken.feature.autotune.workspace.presentation.explorer.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.components.GlassTextField

@Composable
fun RecommendationSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = "Search recommendations (regex)...",
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
