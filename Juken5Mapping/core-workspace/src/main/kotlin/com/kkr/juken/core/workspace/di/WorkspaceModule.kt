package com.kkr.juken.core.workspace.di

import com.kkr.juken.core.workspace.usecase.*
import com.kkr.juken.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WorkspaceModule {

    @Binds @Singleton
    abstract fun bindRestoreWorkspaceLayoutUseCase(impl: RestoreWorkspaceLayoutUseCaseImpl): RestoreWorkspaceLayoutUseCase

    @Binds @Singleton
    abstract fun bindSaveWorkspaceLayoutUseCase(impl: SaveWorkspaceLayoutUseCaseImpl): SaveWorkspaceLayoutUseCase

    @Binds @Singleton
    abstract fun bindLoadRecommendationsUseCase(impl: LoadRecommendationsUseCaseImpl): LoadRecommendationsUseCase

    @Binds @Singleton
    abstract fun bindAcceptRecommendationUseCase(impl: AcceptRecommendationUseCaseImpl): AcceptRecommendationUseCase

    @Binds @Singleton
    abstract fun bindRejectRecommendationUseCase(impl: RejectRecommendationUseCaseImpl): RejectRecommendationUseCase
}
