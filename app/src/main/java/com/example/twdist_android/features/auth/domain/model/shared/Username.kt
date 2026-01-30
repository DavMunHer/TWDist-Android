package com.example.twdist_android.features.auth.domain.model.shared


sealed class UsernameError {
    data object TooShort : UsernameError()
}

@JvmInline
value class Username private constructor(val value: String) {
    companion object {
        private const val MIN_LENGTH = 3

        fun create(raw: String): Result<Username> =
            if (raw.length < MIN_LENGTH)
                Result.failure(
                    UsernameException(UsernameError.TooShort)
                )
            else
                Result.success(Username(raw))
    }
    fun asPlainText(): String = value
}

class UsernameException(val error: UsernameError) : IllegalArgumentException()

