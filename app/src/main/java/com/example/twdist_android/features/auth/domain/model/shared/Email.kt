package com.example.twdist_android.features.auth.domain.model.shared

@JvmInline
value class Email private constructor(
    val value: String
) {
    companion object {
        private val EMAIL_REGEX =
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

        fun create(raw: String): Email {
            require(raw.isNotBlank()) { "Email cannot be blank" }
            require(EMAIL_REGEX.matches(raw)) { "Invalid email format" }
            return Email(raw)
        }
    }
}
