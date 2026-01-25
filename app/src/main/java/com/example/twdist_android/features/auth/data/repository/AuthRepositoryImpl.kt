package com.example.twdist_android.features.auth.data.repository

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
