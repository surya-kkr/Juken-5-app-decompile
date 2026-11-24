package com.kkr.juken.app.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.kkr.juken.core.protocol.ProtocolCodec
import com.kkr.juken.core.sync.LiveSessionManager
import com.kkr.juken.core.sync.SyncDataListener
import com.kkr.juken.core.sync.SyncEngine
import com.kkr.juken.core.transport.*
import com.kkr.juken.core.transport.bluetooth.*
import com.kkr.juken.domain.repository.EcuRepository
import com.kkr.juken.domain.state.ApplicationStateManager
import com.kkr.juken.mock.ecu.EcuSimulator
import com.kkr.juken.mock.ecu.SimulatorTransport
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides @Singleton
    fun provideGlobalScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Provides @Singleton
    fun provideProtocolCodec(): ProtocolCodec = ProtocolCodec.createJuken5()

    @Provides @Singleton
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter? {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }

    @Provides @Singleton
    fun provideBluetoothPermissionManager(
        @ApplicationContext context: Context,
        adapter: BluetoothAdapter?
    ): BluetoothPermissionManager = AndroidBluetoothPermissionManager(context, adapter)

    @Provides @Singleton
    fun provideBluetoothStateMonitor(
        @ApplicationContext context: Context,
        permissionManager: BluetoothPermissionManager,
        sessionStateManager: com.kkr.juken.domain.state.SessionStateManager
    ): BluetoothStateMonitor = AndroidBluetoothStateMonitor(context, permissionManager, sessionStateManager)

    @Provides @Singleton
    fun provideEcuSimulator(): EcuSimulator = EcuSimulator()

    @Provides @Singleton
    fun provideSimulatorTransport(simulator: EcuSimulator): SimulatorTransport = SimulatorTransport(simulator)

    @Provides @Singleton
    fun provideTransport(
        @ApplicationContext context: Context,
        adapter: BluetoothAdapter?,
        permissionManager: BluetoothPermissionManager,
        sessionStateManager: com.kkr.juken.domain.state.SessionStateManager,
        scope: CoroutineScope,
        appState: ApplicationStateManager,
        simulatorTransport: SimulatorTransport
    ): Transport {
        val btTransport = BluetoothTransport(adapter, permissionManager, sessionStateManager, scope)
        return DelegatingTransport(
            btTransport = btTransport,
            simulatorTransport = simulatorTransport,
            transportTypeSelector = appState.settings.state.map { it.selectedTransport }
        )
    }

    @Provides @Singleton
    fun provideBluetoothScanner(
        @ApplicationContext context: Context,
        adapter: BluetoothAdapter?,
        permissionManager: BluetoothPermissionManager,
        sessionStateManager: com.kkr.juken.domain.state.SessionStateManager
    ): BluetoothScanner = AndroidBluetoothScanner(context, adapter, permissionManager, sessionStateManager)

    @Provides @Singleton
    fun provideConnectionSession(
        transport: Transport,
        scope: CoroutineScope
    ): ConnectionSession = ConnectionSession.create(transport, scope)

    @Provides @Singleton
    fun provideLiveSessionManager(
        session: ConnectionSession,
        codec: ProtocolCodec,
        scope: CoroutineScope
    ): LiveSessionManager = LiveSessionManager(session, codec, scope)

    @Provides @Singleton
    fun provideSyncEngine(
        codec: ProtocolCodec,
        session: ConnectionSession,
        listener: SyncDataListener,
        scope: CoroutineScope
    ): SyncEngine = SyncEngine(codec, session, listener, scope = scope)
}
