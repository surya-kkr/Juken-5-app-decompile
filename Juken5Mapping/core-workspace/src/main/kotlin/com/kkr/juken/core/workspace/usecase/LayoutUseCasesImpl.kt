package com.kkr.juken.core.workspace.usecase

import com.kkr.juken.core.workspace.coordinator.WorkspaceCoordinator
import com.kkr.juken.domain.model.workspace.WorkspaceLayout
import com.kkr.juken.domain.usecase.RestoreWorkspaceLayoutUseCase
import com.kkr.juken.domain.usecase.SaveWorkspaceLayoutUseCase
import javax.inject.Inject

internal class RestoreWorkspaceLayoutUseCaseImpl @Inject constructor(
    private val coordinator: WorkspaceCoordinator
) : RestoreWorkspaceLayoutUseCase {
    override suspend fun invoke(): WorkspaceLayout = coordinator.restoreLayout()
}

internal class SaveWorkspaceLayoutUseCaseImpl @Inject constructor(
    private val coordinator: WorkspaceCoordinator
) : SaveWorkspaceLayoutUseCase {
    override suspend fun invoke(layout: WorkspaceLayout) = coordinator.saveLayout(layout)
}
