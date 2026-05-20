package com.example.twdist_android.features.auth.domain.repository

import com.example.twdist_android.features.auth.domain.model.LoginCredentials
import com.example.twdist_android.features.auth.domain.model.RegisterCredentials
import com.example.twdist_android.features.auth.domain.model.RegisteredUser

interface AuthRepository {
    suspend fun register(credentials: RegisterCredentials): Result<RegisteredUser>
    suspend fun sendLogin(credentials: LoginCredentials)
    suspend fun getCurrentUser(): Result<RegisteredUser>
    suspend fun refreshSession(): Result<Unit>
    suspend fun logout()
    suspend fun clearLocalSession()
}