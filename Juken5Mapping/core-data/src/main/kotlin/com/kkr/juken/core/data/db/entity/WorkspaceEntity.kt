package com.kkr.juken.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workspaces")
data class WorkspaceEntity(
    @PrimaryKey val id: String,
    val leftPanelWidthFraction: Float,
    val rightPanelWidthFraction: Float,
    val bottomPanelHeightFraction: Float,
    val isLeftPanelCollapsed: Boolean,
    val isRightPanelCollapsed: Boolean,
    val isBottomPanelCollapsed: Boolean,
    val lastSelectedTool: String,
    val lastSelectedOverlay: String
)
