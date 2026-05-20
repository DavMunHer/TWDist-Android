package com.example.twdist_android.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.auth.application.usecases.LogoutUseCase
import com.example.twdist_android.features.auth.application.usecases.RestoreSessionUseCase
import com.example.twdist_android.features.auth.domain.model.SessionStatus
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val restoreSessionUseCase: RestoreSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
    authSessionManager: AuthSessionManager
) : ViewModel() {

    val sessionStatus: StateFlow<SessionStatus> = authSessionManager.sessionStatus

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut.asStateFlow()

    init {
        viewModelScope.launch {
            restoreSessionUseCase()
        }
    }

    fun logout() {
        if (_isLoggingOut.value) return
        _isLoggingOut.value = true
        viewModelScope.launch {
            try {
                logoutUseCase()
            } finally {
                _isLoggingOut.value = false
            }
        }
    }
}
