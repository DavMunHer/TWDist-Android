package com.example.twdist_android.features.auth.domain.repository

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.domain.model.LoginCredentials
import com.example.twdist_android.features.auth.domain.model.User

interface AuthRepository {
    // This is for defining the actions that this feature can do regarding the data layer (requests to back and stuff)

    suspend fun register(req: RegisterRequestDto): User; // For returning the User model type, we will use the toDomain() func from the mapper (UserResponse)
    suspend fun sendLogin(credentials: LoginCredentials): Unit
}