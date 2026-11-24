package com.kkr.juken.domain.state

import com.kkr.juken.domain.model.state.*
import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import kotlinx.coroutines.flow.StateFlow

interface SettingsStateManager {
    val state: StateFlow<SettingsState>
    suspend fun updateTheme(theme: AppTheme)
    suspend fun setDeveloperMode(enabled: Boolean)
    suspend fun setTransport(type: TransportType)
}

interface SessionStateManager {
    val state: StateFlow<SessionState>
    fun onConnected(address: String, transport: TransportType)
    fun onDisconnected()
    fun updateHeartbeat(active: Boolean)
    fun updateStatus(status: com.kkr.juken.domain.model.state.SessionStatus)
}

interface WorkspaceStateManager {
    val state: StateFlow<WorkspaceState>
    fun openProject(metadata: ProjectMetadata)
    fun markDirty(dirty: Boolean)
    fun updateLayout(layout: WorkspaceLayout)
}

interface NavigationStateManager {
    val state: StateFlow<NavigationState>
    fun navigateTo(route: String)
    fun popBackstack()
}

interface DeveloperStateManager {
    val state: StateFlow<DeveloperState>
    fun togglePacketInspector(enabled: Boolean)
}

interface PermissionStateManager {
    val state: StateFlow<PermissionState>
    fun updatePermissions(permissions: PermissionState)
}

interface WindowStateManager {
    val state: StateFlow<WindowState>
    fun updateWindowSize(width: Int, height: Int, density: Float)
}

interface ApplicationStateManager {
    val settings: SettingsStateManager
    val session: SessionStateManager
    val wsManager: WorkspaceStateManager
    val navigation: NavigationStateManager
    val developer: DeveloperStateManager
    val permissions: PermissionStateManager
    val window: WindowStateManager
}
