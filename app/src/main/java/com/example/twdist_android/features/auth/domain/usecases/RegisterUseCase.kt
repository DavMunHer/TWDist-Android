package com.example.twdist_android.features.auth.domain.usecases

import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.data.dto.UserResponseDto
import com.example.twdist_android.features.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    fun execute(req: RegisterRequestDto): UserResponseDto {
        // TODO: Make http request
    }
}