package com.example.twdist_android.features.auth.domain.model.shared

sealed class PasswordError {
    data object TooShort : PasswordError()
}

@JvmInline
value class Password private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 8

        fun create(raw: String): Result<Password> =
            if (raw.length < MIN_LENGTH)
                Result.failure(
                    PasswordException(PasswordError.TooShort)
                )
            else
                Result.success(Password(raw))
    }
}

class PasswordException(val error: PasswordError) : IllegalArgumentException()

