package com.example.twdist_android.core.network

import com.example.twdist_android.features.auth.application.usecases.RefreshSessionUseCase
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshAuthenticator @Inject constructor(
    private val refreshSessionUseCase: Lazy<RefreshSessionUseCase>,
    private val authRepository: Lazy<AuthRepository>,
    private val authSessionManager: Lazy<AuthSessionManager>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        if (isAuthEndpoint(response.request)) return null

        val request = response.request
        if (request.header(RETRY_HEADER) != null) {
            runBlocking {
                authRepository.get().clearLocalSession()
                authSessionManager.get().setUnauthenticated()
            }
            return null
        }

        val refreshResult = runBlocking { refreshSessionUseCase.get().invoke() }
        if (refreshResult.isFailure) {
            runBlocking {
                authRepository.get().clearLocalSession()
                authSessionManager.get().setUnauthenticated()
            }
            return null
        }

        return request.newBuilder()
            .header(RETRY_HEADER, "true")
            .build()
    }

    private fun isAuthEndpoint(request: Request): Boolean {
        val path = request.url.encodedPath
        return path.endsWith("/auth/login") ||
            path.endsWith("/auth/refresh") ||
            path.endsWith("/auth/logout") ||
            path.endsWith("/users/create")
    }

    companion object {
        private const val RETRY_HEADER = "X-Auth-Retry"
    }
}
