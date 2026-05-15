package com.example.twdist_android.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twdist_android.features.auth.application.usecases.RestoreSessionUseCase
import com.example.twdist_android.features.auth.domain.model.SessionStatus
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val restoreSessionUseCase: RestoreSessionUseCase,
    authSessionManager: AuthSessionManager
) : ViewModel() {

    val sessionStatus: StateFlow<SessionStatus> = authSessionManager.sessionStatus

    init {
        viewModelScope.launch {
            restoreSessionUseCase()
        }
    }
}
