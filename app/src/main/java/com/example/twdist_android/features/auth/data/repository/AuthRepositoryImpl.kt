package com.example.twdist_android.features.auth.data.repository

// This file will manage all the data (domain) layer, therefore making the http requests and this kinda stuff
// This layer is not responsible for changing the logic of the UI, only the persistent data (db located) and requests
// This implements the AuthRepository interface and applies the logic needed for each method

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.data.mapper.toDomain
import com.example.twdist_android.features.auth.data.remote.AuthApi
import com.example.twdist_android.features.auth.domain.model.User
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun register(req: RegisterRequestDto): User {
        return withContext(Dispatchers.IO) {
            val dto = api.register(req)
            dto.body()!!.toDomain()
        }
    }

    override suspend fun sendLogin(req: LoginRequestDto): Unit {
        return withContext(Dispatchers.IO) {
            api.login(req);
        }
    }
}
