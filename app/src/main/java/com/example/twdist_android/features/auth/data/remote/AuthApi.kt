package com.example.twdist_android.features.auth.data.remote

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.data.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("users/create")
    suspend fun register(@Body req: RegisterRequestDto): Response<UserResponseDto>

    @POST("auth/login")
    suspend fun login(@Body req: LoginRequestDto): Response<Unit>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<UserResponseDto>

    @POST("auth/refresh")
    suspend fun refresh(): Response<Unit>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
