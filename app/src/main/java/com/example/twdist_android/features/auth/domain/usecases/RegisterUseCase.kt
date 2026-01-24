package com.example.twdist_android.features.auth.domain.usecases

import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.domain.model.User
import com.example.twdist_android.features.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(req: RegisterRequestDto): User {
        return authRepository.register(req)
    }
}