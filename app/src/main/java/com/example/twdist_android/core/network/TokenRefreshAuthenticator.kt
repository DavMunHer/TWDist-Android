package com.example.twdist_android.core.network

import com.example.twdist_android.BuildConfig
import com.example.twdist_android.di.RefreshHttpClient
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager
import okhttp3.Authenticator
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshAuthenticator @Inject constructor(
    @param:RefreshHttpClient private val refreshClient: OkHttpClient,
    private val cookieJar: CookieJarImpl,
    private val authSessionManager: AuthSessionManager
) : Authenticator {

    private val refreshUrl = BuildConfig.BASE_URL.toHttpUrl().resolve("auth/refresh")!!

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        if (isAuthEndpoint(response.request)) return null

        val request = response.request
        if (request.header(RETRY_HEADER) != null) {
            clearSession()
            return null
        }

        if (!refreshSync()) {
            clearSession()
            return null
        }

        return request.newBuilder()
            .header(RETRY_HEADER, "true")
            .build()
    }

    private fun refreshSync(): Boolean {
        val request = Request.Builder()
            .url(refreshUrl)
            .post(ByteArray(0).toRequestBody(null))
            .build()
        return refreshClient.newCall(request).execute().use { it.isSuccessful }
    }

    private fun clearSession() {
        cookieJar.clearAll()
        authSessionManager.setUnauthenticated()
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
