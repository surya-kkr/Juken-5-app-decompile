package com.kkr.juken.core.state.internal

import com.kkr.juken.domain.state.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ApplicationStateManagerImpl @Inject constructor(
    override val settings: SettingsStateManager,
    override val session: SessionStateManager,
    override val wsManager: WorkspaceStateManager,
    override val navigation: NavigationStateManager,
    override val developer: DeveloperStateManager,
    override val permissions: PermissionStateManager,
    override val window: WindowStateManager
) : ApplicationStateManager
