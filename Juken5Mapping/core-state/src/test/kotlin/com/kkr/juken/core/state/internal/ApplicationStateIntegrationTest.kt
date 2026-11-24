package com.kkr.juken.core.state.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.kkr.juken.domain.model.state.AppTheme
import com.kkr.juken.domain.model.state.ProjectMetadata
import com.kkr.juken.domain.state.ApplicationStateManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class ApplicationStateIntegrationTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var testScope: TestScope
    private lateinit var orchestrator: ApplicationStateManager

    @Before
    fun setup() {
        testScope = TestScope(UnconfinedTestDispatcher() + Job())
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(tmp.root, "test.preferences_pb") }
        )

        orchestrator = ApplicationStateManagerImpl(
            settings = SettingsStateManagerImpl(dataStore, testScope),
            session = SessionStateManagerImpl(),
            wsManager = WorkspaceStateManagerImpl(dataStore, testScope),
            navigation = NavigationStateManagerImpl(dataStore, testScope),
            developer = DeveloperStateManagerImpl(dataStore, testScope),
            permissions = PermissionStateManagerImpl(),
            window = WindowStateManagerImpl()
        )
    }

    @Test
    fun `settings and navigation should persist via dataStore`() = runTest {
        // Collect to start WhileSubscribed
        val settingsJob = launch { orchestrator.settings.state.collect() }
        val navJob = launch { orchestrator.navigation.state.collect() }

        orchestrator.settings.updateTheme(AppTheme.OLED)
        orchestrator.navigation.navigateTo("dashboard")

        advanceUntilIdle()

        assertEquals(AppTheme.OLED, orchestrator.settings.state.value.theme)
        assertEquals("dashboard", orchestrator.navigation.state.value.currentRoute)
        
        settingsJob.cancel()
        navJob.cancel()
    }

    @Test
    fun `opening project should update workspace state`() = runTest {
        val wsJob = launch { orchestrator.wsManager.state.collect() }

        orchestrator.settings.updateTheme(AppTheme.OLED)
        val project = ProjectMetadata("id1", "Project X", 123)
        orchestrator.wsManager.openProject(project)

        advanceUntilIdle()

        assertEquals("Project X", orchestrator.wsManager.state.value.currentProject?.name)
        
        wsJob.cancel()
    }
}
