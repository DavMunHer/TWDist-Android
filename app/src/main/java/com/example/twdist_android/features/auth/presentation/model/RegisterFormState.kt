package com.example.twdist_android.features.auth.presentation.model

data class RegisterFormState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val usernameError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
