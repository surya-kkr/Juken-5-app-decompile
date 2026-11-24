package com.kkr.juken.core.data.di

import com.kkr.juken.core.data.DefaultEcuRepository
import com.kkr.juken.core.data.repository.*
import com.kkr.juken.core.sync.SyncDataListener
import com.kkr.juken.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindEcuRepository(impl: EcuRepositoryImpl): EcuRepository

    @Binds @Singleton
    abstract fun bindSyncDataListener(impl: DefaultEcuRepository): SyncDataListener

    @Binds @Singleton
    abstract fun bindWorkspaceRepository(impl: WorkspaceRepositoryImpl): WorkspaceRepository

    @Binds @Singleton
    abstract fun bindRecommendationRepository(impl: RecommendationRepositoryImpl): RecommendationRepository

    @Binds @Singleton
    abstract fun bindProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository

    @Binds @Singleton
    abstract fun bindMapRepository(impl: MapRepositoryImpl): MapRepository

    @Binds @Singleton
    abstract fun bindCalibrationRepository(impl: CalibrationRepositoryImpl): CalibrationRepository

    @Binds @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}
