package com.example.twdist_android.features.auth.data.remote

// This layer will use the DTOs and the UserApi (property of the class that will be used for making the requests)

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.data.dto.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body req: RegisterRequestDto): retrofit2.Response<UserResponseDto>

    @POST("auth/login")
    suspend fun login(@Body req: LoginRequestDto): retrofit2.Response<Unit>
}
