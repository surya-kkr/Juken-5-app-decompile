package com.kkr.juken.core.state.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.kkr.juken.domain.model.state.ProjectMetadata
import com.kkr.juken.domain.model.state.WorkspaceState
import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import com.kkr.juken.domain.state.WorkspaceStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class WorkspaceStateManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) : WorkspaceStateManager {

    private val lastProjectIdKey = stringPreferencesKey("last_project_id")
    private val lastProjectNameKey = stringPreferencesKey("last_project_name")
    
    private val leftPanelWidthKey = floatPreferencesKey("layout_left_width")
    private val rightPanelWidthKey = floatPreferencesKey("layout_right_width")
    private val bottomPanelHeightKey = floatPreferencesKey("layout_bottom_height")
    private val leftPanelCollapsedKey = booleanPreferencesKey("layout_left_collapsed")
    private val rightPanelCollapsedKey = booleanPreferencesKey("layout_right_collapsed")
    private val bottomPanelCollapsedKey = booleanPreferencesKey("layout_bottom_collapsed")

    private val _workspaceState = MutableStateFlow(WorkspaceState())
    override val state: StateFlow<WorkspaceState> = combine(
        dataStore.data, _workspaceState
    ) { prefs, memory ->
        val id = prefs[lastProjectIdKey]
        val name = prefs[lastProjectNameKey]
        val project = if (id != null && name != null) ProjectMetadata(id, name, 0) else null
        
        val layout = WorkspaceLayout(
            leftPanelWidthFraction = prefs[leftPanelWidthKey] ?: 0.2f,
            rightPanelWidthFraction = prefs[rightPanelWidthKey] ?: 0.2f,
            bottomPanelHeightFraction = prefs[bottomPanelHeightKey] ?: 0.25f,
            isLeftPanelCollapsed = prefs[leftPanelCollapsedKey] ?: false,
            isRightPanelCollapsed = prefs[rightPanelCollapsedKey] ?: false,
            isBottomPanelCollapsed = prefs[bottomPanelCollapsedKey] ?: false
        )
        
        memory.copy(
            currentProject = memory.currentProject ?: project,
            layout = layout
        )
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000), WorkspaceState())

    override fun openProject(metadata: ProjectMetadata) {
        _workspaceState.value = _workspaceState.value.copy(currentProject = metadata)
        scope.launch {
            dataStore.edit {
                it[lastProjectIdKey] = metadata.id
                it[lastProjectNameKey] = metadata.name
            }
        }
    }

    override fun markDirty(dirty: Boolean) {
        _workspaceState.value = _workspaceState.value.copy(isEditorDirty = dirty)
    }

    override fun updateLayout(layout: WorkspaceLayout) {
        scope.launch {
            dataStore.edit {
                it[leftPanelWidthKey] = layout.leftPanelWidthFraction
                it[rightPanelWidthKey] = layout.rightPanelWidthFraction
                it[bottomPanelHeightKey] = layout.bottomPanelHeightFraction
                it[leftPanelCollapsedKey] = layout.isLeftPanelCollapsed
                it[rightPanelCollapsedKey] = layout.isRightPanelCollapsed
                it[bottomPanelCollapsedKey] = layout.isBottomPanelCollapsed
            }
        }
    }
}
