package com.example.twdist_android.features.auth.data.repository

import com.example.twdist_android.core.network.CookieJarImpl
import com.example.twdist_android.features.auth.data.dto.UserResponseDto
import com.example.twdist_android.features.auth.data.remote.AuthApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {
    private val api: AuthApi = mockk()
    private val cookieJar: CookieJarImpl = mockk(relaxed = true)
    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        repository = AuthRepositoryImpl(api, json, cookieJar)
    }

    @Test
    fun `getCurrentUser returns mapped user on success`() = runTest {
        val dto = UserResponseDto(id = 1L, username = "test", email = "user@email.com")
        coEvery { api.getCurrentUser() } returns Response.success(dto)

        val result = repository.getCurrentUser()

        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull()?.id)
        assertEquals("test", result.getOrNull()?.username)
        assertEquals("user@email.com", result.getOrNull()?.email)
    }

    @Test
    fun `getCurrentUser returns failure on error response`() = runTest {
        coEvery { api.getCurrentUser() } returns Response.error(
            401,
            "".toResponseBody()
        )

        val result = repository.getCurrentUser()

        assertTrue(result.isFailure)
    }

    @Test
    fun `refreshSession returns success when api succeeds`() = runTest {
        coEvery { api.refresh() } returns Response.success(Unit)

        val result = repository.refreshSession()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `clearLocalSession clears cookie jar`() = runTest {
        repository.clearLocalSession()

        verify(exactly = 1) { cookieJar.clearAll() }
    }
}
