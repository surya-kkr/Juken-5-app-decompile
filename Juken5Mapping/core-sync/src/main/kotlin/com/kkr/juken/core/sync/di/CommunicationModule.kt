package com.kkr.juken.core.sync.di

import com.kkr.juken.core.sync.usecase.*
import com.kkr.juken.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CommunicationModule {

    @Binds @Singleton
    abstract fun bindConnectEcuUseCase(impl: ConnectEcuUseCaseImpl): ConnectEcuUseCase

    @Binds @Singleton
    abstract fun bindDisconnectEcuUseCase(impl: DisconnectEcuUseCaseImpl): DisconnectEcuUseCase

    @Binds @Singleton
    abstract fun bindSynchronizeEcuUseCase(impl: SynchronizeEcuUseCaseImpl): SynchronizeEcuUseCase
}
