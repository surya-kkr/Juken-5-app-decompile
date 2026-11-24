package com.kkr.juken.feature.connection.presentation.permissions

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkr.juken.core.ui.components.GlassBackground
import com.kkr.juken.core.ui.components.GlassButton
import com.kkr.juken.core.ui.components.GlassCard
import com.kkr.juken.core.ui.theme.JukenTokens

@Composable
fun PermissionScreen(
    onPermissionsGranted: () -> Unit,
    viewModel: PermissionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val btState by viewModel.bluetoothPermissionState.collectAsState()
    
    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        viewModel.onPermissionsResult(result)
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                PermissionEffect.NavigateToScanner -> onPermissionsGranted()
            }
        }
    }

    if (state.bluetoothGranted) {
        LaunchedEffect(Unit) {
            onPermissionsGranted()
        }
    }

    GlassBackground {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            GlassCard(modifier = Modifier.padding(24.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "PERMISSIONS REQUIRED",
                        color = JukenTokens.Colors.Primary,
                        fontSize = 20.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val description = if (btState.isPermanentlyDenied) {
                        "Bluetooth permissions are permanently denied. Please enable them in app settings to continue."
                    } else {
                        "Juken 5 Mapping requires Bluetooth permissions to connect to your ECU. Please grant these permissions to continue."
                    }

                    Text(
                        description,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    val buttonText = if (btState.isPermanentlyDenied) "OPEN SETTINGS" else "GRANT PERMISSIONS"

                    GlassButton(
                        text = buttonText,
                        onClick = { 
                            if (btState.isPermanentlyDenied) {
                                viewModel.onOpenSettings()
                            } else {
                                launcher.launch(permissionsToRequest)
                            }
                        }
                    )
                }
            }
        }
    }
}
