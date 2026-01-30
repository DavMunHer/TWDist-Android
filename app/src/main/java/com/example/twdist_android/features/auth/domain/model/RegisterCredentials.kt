package com.example.twdist_android.features.auth.domain.model

import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.model.shared.Username

data class RegisterCredentials (
    val email: Email,
    val username: Username,
    val password: Password
)