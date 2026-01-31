package com.example.twdist_android.features.auth.domain.usecases

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.domain.model.LoginCredentials
import com.example.twdist_android.features.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(req: LoginCredentials): Unit {
        // TODO: Make domain validation if needed -> Email & password is valid, etc.
        return authRepository.sendLogin(req)
    }
}