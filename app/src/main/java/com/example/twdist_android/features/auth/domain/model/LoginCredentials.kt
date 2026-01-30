package com.example.twdist_android.features.auth.domain.model

import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password

data class LoginCredentials(
    val email: Email,
    val password: Password
)
