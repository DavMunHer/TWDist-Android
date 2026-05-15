package com.example.twdist_android.features.auth.application.usecases

import com.example.twdist_android.features.auth.domain.repository.AuthRepository
class RefreshSessionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> = authRepository.refreshSession()
}
