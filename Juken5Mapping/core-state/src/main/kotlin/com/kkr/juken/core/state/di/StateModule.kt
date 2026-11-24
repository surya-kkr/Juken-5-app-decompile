package com.kkr.juken.core.state.di

import com.kkr.juken.core.state.internal.*
import com.kkr.juken.domain.state.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class StateModule {

    @Binds @Singleton
    abstract fun bindSettingsStateManager(impl: SettingsStateManagerImpl): SettingsStateManager

    @Binds @Singleton
    abstract fun bindSessionStateManager(impl: SessionStateManagerImpl): SessionStateManager

    @Binds @Singleton
    abstract fun bindWorkspaceStateManager(impl: WorkspaceStateManagerImpl): WorkspaceStateManager

    @Binds @Singleton
    abstract fun bindNavigationStateManager(impl: NavigationStateManagerImpl): NavigationStateManager

    @Binds @Singleton
    abstract fun bindDeveloperStateManager(impl: DeveloperStateManagerImpl): DeveloperStateManager

    @Binds @Singleton
    abstract fun bindPermissionStateManager(impl: PermissionStateManagerImpl): PermissionStateManager

    @Binds @Singleton
    abstract fun bindWindowStateManager(impl: WindowStateManagerImpl): WindowStateManager

    @Binds @Singleton
    abstract fun bindApplicationStateManager(impl: ApplicationStateManagerImpl): ApplicationStateManager
}
