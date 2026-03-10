package com.example.twdist_android.features.auth.presentation.mapper

import com.example.twdist_android.features.auth.domain.model.RegisterCredentials
import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.model.shared.Username
import com.example.twdist_android.features.auth.presentation.model.RegisterFormState

fun RegisterFormState.toCredentials(): Result<RegisterCredentials> {
    val emailResult = Email.create(email)
    val usernameResult = Username.create(username)
    val passwordResult = Password.create(password)

    val emailFailure = emailResult.exceptionOrNull()
    val usernameFailure = usernameResult.exceptionOrNull()
    val passwordFailure = passwordResult.exceptionOrNull()

    // Return the first validation failure found, if any
    val firstFailure = emailFailure ?: usernameFailure ?: passwordFailure
    if (firstFailure != null) return Result.failure(firstFailure)

    return Result.success(
        RegisterCredentials(
            email = emailResult.getOrThrow(),
            username = usernameResult.getOrThrow(),
            password = passwordResult.getOrThrow()
        )
    )
}

