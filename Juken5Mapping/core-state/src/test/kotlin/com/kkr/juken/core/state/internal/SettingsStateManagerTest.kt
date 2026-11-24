package com.kkr.juken.core.state.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.kkr.juken.domain.model.state.AppTheme
import com.kkr.juken.domain.model.state.TransportType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsStateManagerTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var manager: SettingsStateManagerImpl
    private val testScope = TestScope(UnconfinedTestDispatcher() + Job())

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(tmp.root, "test_${UUID.randomUUID()}.preferences_pb") }
        )
        manager = SettingsStateManagerImpl(dataStore, testScope)
    }
    
    @After
    fun teardown() {
        testScope.cancel()
    }

    @Test
    fun `default state should be correct`() = runTest {
        val job = launch { manager.state.collect() }
        advanceUntilIdle()
        assertEquals(AppTheme.DARK, manager.state.value.theme)
        job.cancel()
    }

    @Test
    fun `updateTheme should persist and emit new state`() = runTest {
        val job = launch { manager.state.collect() }
        manager.updateTheme(AppTheme.LIGHT)
        advanceUntilIdle()
        assertEquals(AppTheme.LIGHT, manager.state.value.theme)
        job.cancel()
    }
}
