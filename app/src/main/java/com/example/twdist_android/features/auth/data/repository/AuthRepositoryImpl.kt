package com.example.twdist_android.features.auth.data.repository

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.data.remote.AuthApi
import com.example.twdist_android.features.auth.domain.model.User
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override fun createUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun sendLogin(req: LoginRequestDto): Unit {
        return withContext(Dispatchers.IO) {
            api.login(req); // This should set the http only cookie
        }
    }

}