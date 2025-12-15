package com.example.twdist_android.features.auth.domain.model

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String
)