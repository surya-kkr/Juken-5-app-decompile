package com.kkr.juken.core.data.repository

import com.kkr.juken.core.sync.LiveSessionManager
import com.kkr.juken.core.transport.ConnectionSession
import com.kkr.juken.core.transport.ConnectionState
import com.kkr.juken.domain.model.ui.ConnectionUiState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EcuRepositoryTest {

    private val session = mockk<ConnectionSession>(relaxed = true)
    private val liveManager = mockk<LiveSessionManager>(relaxed = true)
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var repository: EcuRepositoryImpl

    private val sessionState = MutableStateFlow(ConnectionState.IDLE)

    @Before
    fun setup() {
        every { session.state } returns sessionState
        repository = EcuRepositoryImpl(session, liveManager, testScope)
    }

    @Test
    fun `Repository should map ConnectionState to ConnectionUiState`() = runTest {
        sessionState.value = ConnectionState.CONNECTING
        assertEquals(ConnectionUiState.Connecting, repository.connectionState.value)

        sessionState.value = ConnectionState.CONNECTED
        assertEquals(ConnectionUiState.Connected, repository.connectionState.value)
    }
}
