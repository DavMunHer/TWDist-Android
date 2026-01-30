package com.example.twdist_android.features.auth.presentation.mapper

import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.EmailError
import com.example.twdist_android.features.auth.domain.model.shared.EmailException
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.model.shared.PasswordError
import com.example.twdist_android.features.auth.domain.model.shared.PasswordException

@JvmName("emailToUiError")
fun Result<Email>.toUiError(): String? =
    exceptionOrNull()
        ?.let { it as? EmailException }
        ?.error
        ?.let {
            when (it) {
                EmailError.Blank -> "Email cannot be blank"
                EmailError.InvalidFormat -> "Invalid format for email address"
            }
        }

@JvmName("passwordToUiError")
fun Result<Password>.toUiError(): String? =
    exceptionOrNull()
        ?.let { it as? PasswordException }
        ?.error
        ?.let {
            when (it) {
                PasswordError.TooShort ->
                    "The password must be at least 8 characters long"
            }
        }
