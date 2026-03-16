package com.example.twdist_android.features.auth.data.mapper

import com.example.twdist_android.features.auth.data.dto.UserResponseDto
import com.example.twdist_android.features.auth.domain.model.RegisteredUser

fun UserResponseDto.toDomain(): RegisteredUser {
    return RegisteredUser(
        id = id,
        username = username,
        email = email
    )
}
