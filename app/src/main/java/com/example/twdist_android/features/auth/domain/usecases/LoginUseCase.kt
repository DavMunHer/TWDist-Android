package com.example.twdist_android.features.auth.domain.usecases

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(req: LoginRequestDto): Unit {
        return authRepository.sendLogin(req)
    }
}