package com.example.twdist_android.features.auth.domain.model

@JvmInline
value class Password private constructor(
    val value: String
) {
    companion object {
        private const val MIN_LENGTH = 8

        fun create(raw: String): Password {
            require(raw.length >= MIN_LENGTH) {
                "Password must be at least $MIN_LENGTH characters"
            }
            return Password(raw)
        }
    }
}
