package com.example.twdist_android.features.auth.data.remote

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body req: LoginRequestDto): retrofit2.Response<Unit>

}