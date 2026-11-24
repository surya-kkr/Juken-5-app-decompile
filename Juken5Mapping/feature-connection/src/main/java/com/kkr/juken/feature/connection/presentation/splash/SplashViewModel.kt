package com.kkr.juken.feature.connection.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkr.juken.domain.state.ApplicationStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appState: ApplicationStateManager
) : ViewModel() {

    private val _effects = MutableSharedFlow<SplashEffect>()
    val effects = _effects.asSharedFlow()

    init {
        startInitialization()
    }

    private fun startInitialization() {
        viewModelScope.launch {
            // Give branding a moment
            delay(1500)
            
            val permissions = appState.permissions.state.value
            if (permissions.bluetoothGranted) {
                _effects.emit(SplashEffect.NavigateToScanner)
            } else {
                _effects.emit(SplashEffect.NavigateToPermissions)
            }
        }
    }
}

sealed interface SplashEffect {
    object NavigateToScanner : SplashEffect
    object NavigateToPermissions : SplashEffect
}
