package com.example.twdist_android.features.auth.domain.model.shared

sealed class EmailError {
    data object Blank : EmailError()
    data object InvalidFormat : EmailError()
}

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        fun create(raw: String): Result<Email> =
            when {
                raw.isBlank() ->
                    Result.failure(EmailException(EmailError.Blank))

                !raw.contains("@") ->
                    Result.failure(EmailException(EmailError.InvalidFormat))

                else ->
                    Result.success(Email(raw))
            }
    }
    fun asString(): String = value
}

class EmailException(val error: EmailError) : IllegalArgumentException()

