package com.kkr.juken.app.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kkr.juken.domain.state.ApplicationStateManager
import com.kkr.juken.feature.connection.presentation.permissions.PermissionScreen
import com.kkr.juken.feature.connection.presentation.scanner.ScannerScreen
import com.kkr.juken.feature.connection.presentation.splash.SplashScreen
import com.kkr.juken.feature.autotune.workspace.presentation.workspace.AutoTuneWorkspaceScreen
import com.kkr.juken.feature.dashboard.DashboardScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun JukenNavHost(
    appState: ApplicationStateManager
) {
    val navController = rememberNavController()

    LaunchedEffect(appState.navigation.state) {
        appState.navigation.state.collectLatest { state ->
            if (navController.currentDestination?.route != state.currentRoute) {
                navController.navigate(state.currentRoute) {
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToScanner = { appState.navigation.navigateTo("scanner") },
                onNavigateToPermissions = { appState.navigation.navigateTo("permissions") }
            )
        }
        composable("permissions") {
            PermissionScreen(
                onPermissionsGranted = { appState.navigation.navigateTo("scanner") }
            )
        }
        composable("scanner") {
            ScannerScreen(
                onDeviceSelected = { appState.navigation.navigateTo("dashboard") }
            )
        }
        composable("dashboard") {
            DashboardScreen()
        }
        composable("workspace") {
            AutoTuneWorkspaceScreen()
        }
        composable("mapping") { }
        composable("settings") { }
    }
}
