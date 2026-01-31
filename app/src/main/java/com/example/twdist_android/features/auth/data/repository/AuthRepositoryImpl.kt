package com.example.twdist_android.features.auth.data.repository

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.data.mapper.toDomain
import com.example.twdist_android.features.auth.data.remote.AuthApi
import com.example.twdist_android.features.auth.domain.model.LoginCredentials
import com.example.twdist_android.features.auth.domain.model.RegisterCredentials
import com.example.twdist_android.features.auth.domain.model.User
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun register(credentials: RegisterCredentials): User {
        return withContext(Dispatchers.IO) {
            val dto = api.register(
                RegisterRequestDto(
                    email = credentials.email.asString(),
                    username = credentials.username.asString(),
                    password = credentials.password.asPlainText()
                )
            )
            dto.body()!!.toDomain()
        }
    }

    override suspend fun sendLogin(credentials: LoginCredentials): Unit {
        return withContext(Dispatchers.IO) {

            val request = LoginRequestDto(
                email = credentials.email.asString(),
                password = credentials.password.asPlainText()
            )
            api.login(request);
        }
    }
}
