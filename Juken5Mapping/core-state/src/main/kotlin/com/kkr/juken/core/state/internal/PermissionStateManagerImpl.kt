package com.kkr.juken.core.state.internal

import com.kkr.juken.domain.model.state.PermissionState
import com.kkr.juken.domain.state.PermissionStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PermissionStateManagerImpl @Inject constructor() : PermissionStateManager {
    private val _state = MutableStateFlow(PermissionState())
    override val state: StateFlow<PermissionState> = _state.asStateFlow()

    override fun updatePermissions(permissions: PermissionState) {
        _state.value = permissions
    }
}
