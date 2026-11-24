package com.kkr.juken.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kkr.juken.app.navigation.JukenNavHost
import com.kkr.juken.core.transport.BluetoothStateMonitor
import com.kkr.juken.core.ui.theme.JukenTheme
import com.kkr.juken.domain.state.ApplicationStateManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appState: ApplicationStateManager

    @Inject
    lateinit var bluetoothStateMonitor: BluetoothStateMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothStateMonitor.startMonitoring()
        setContent {
            JukenTheme {
                JukenNavHost(appState = appState)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothStateMonitor.stopMonitoring()
    }
}
