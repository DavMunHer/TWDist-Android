package com.example.twdist_android.features.auth.data.mapper

import com.example.twdist_android.features.auth.data.dto.LoginRequestDto
import com.example.twdist_android.features.auth.data.dto.RegisterRequestDto
import com.example.twdist_android.features.auth.data.dto.UserResponseDto
import com.example.twdist_android.features.auth.domain.model.User


fun UserResponseDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        email = email,
        password = ""
    )
}

fun User.toRegisterRequestDto(): RegisterRequestDto {
    return RegisterRequestDto(
        username = username,
        email = email,
        password = password
    )
}

fun User.toLoginRequestDto(): LoginRequestDto {
    return LoginRequestDto(
        email = email,
        password = password
    )
}