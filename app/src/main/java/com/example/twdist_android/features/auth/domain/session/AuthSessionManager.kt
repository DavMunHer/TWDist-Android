package com.example.twdist_android.features.auth.domain.session

import com.example.twdist_android.features.auth.domain.model.RegisteredUser
import com.example.twdist_android.features.auth.domain.model.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSessionManager @Inject constructor() {
    private val _sessionStatus = MutableStateFlow<SessionStatus>(SessionStatus.Checking)
    val sessionStatus: StateFlow<SessionStatus> = _sessionStatus.asStateFlow()

    fun setChecking() {
        _sessionStatus.value = SessionStatus.Checking
    }

    fun setAuthenticated(user: RegisteredUser? = null) {
        _sessionStatus.value = SessionStatus.Authenticated(user)
    }

    fun setUnauthenticated() {
        _sessionStatus.value = SessionStatus.Unauthenticated
    }

    fun isAuthenticated(): Boolean = _sessionStatus.value is SessionStatus.Authenticated
}
