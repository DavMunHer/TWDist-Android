package com.example.twdist_android.features.auth.application.usecases

import com.example.twdist_android.features.auth.domain.model.RegisterCredentials
import com.example.twdist_android.features.auth.domain.model.RegisteredUser
import com.example.twdist_android.features.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credentials: RegisterCredentials): Result<RegisteredUser> {
        return authRepository.register(credentials)
    }
}