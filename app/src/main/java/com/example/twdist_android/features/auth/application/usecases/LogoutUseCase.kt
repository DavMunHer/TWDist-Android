package com.example.twdist_android.features.auth.application.usecases

import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val authSessionManager: AuthSessionManager
) {
    suspend operator fun invoke() {
        authRepository.logout()
        authSessionManager.setUnauthenticated()
    }
}
