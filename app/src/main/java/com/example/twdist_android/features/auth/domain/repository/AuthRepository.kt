package com.example.twdist_android.features.auth.domain.repository

import com.example.twdist_android.features.auth.domain.model.LoginCredentials
import com.example.twdist_android.features.auth.domain.model.RegisterCredentials
import com.example.twdist_android.features.auth.domain.model.RegisteredUser

interface AuthRepository {
    // This is for defining the actions that this feature can do regarding the data layer (requests to back and stuff)

    suspend fun register(credentials: RegisterCredentials): Result<RegisteredUser>
    suspend fun sendLogin(credentials: LoginCredentials)
}