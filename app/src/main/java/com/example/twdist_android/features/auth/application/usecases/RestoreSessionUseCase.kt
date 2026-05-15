package com.example.twdist_android.features.auth.application.usecases

import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager
class RestoreSessionUseCase(
    private val authRepository: AuthRepository,
    private val authSessionManager: AuthSessionManager
) {
    suspend operator fun invoke() {
        authSessionManager.setChecking()

        val currentUser = authRepository.getCurrentUser()
        if (currentUser.isSuccess) {
            authSessionManager.setAuthenticated(currentUser.getOrNull())
            return
        }

        val refresh = authRepository.refreshSession()
        if (refresh.isSuccess) {
            val userAfterRefresh = authRepository.getCurrentUser()
            if (userAfterRefresh.isSuccess) {
                authSessionManager.setAuthenticated(userAfterRefresh.getOrNull())
                return
            }
        }

        authRepository.clearLocalSession()
        authSessionManager.setUnauthenticated()
    }
}
