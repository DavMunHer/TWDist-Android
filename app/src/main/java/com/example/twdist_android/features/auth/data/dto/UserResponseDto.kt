package com.example.twdist_android.features.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val id: Long,
    val username: String,
    val email: String
)
