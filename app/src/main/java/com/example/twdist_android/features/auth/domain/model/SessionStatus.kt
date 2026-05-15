package com.example.twdist_android.features.auth.domain.model

sealed interface SessionStatus {
    data object Checking : SessionStatus

    data class Authenticated(val user: RegisteredUser?) : SessionStatus

    data object Unauthenticated : SessionStatus
}
