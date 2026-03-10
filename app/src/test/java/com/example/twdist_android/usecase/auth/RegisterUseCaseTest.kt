package com.example.twdist_android.usecase.auth

import com.example.twdist_android.features.auth.domain.model.RegisterCredentials
import com.example.twdist_android.features.auth.domain.model.RegisteredUser
import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.model.shared.Username
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import com.example.twdist_android.features.auth.domain.usecases.RegisterUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegisterUseCaseTest {
    private lateinit var registerUseCase: RegisterUseCase
    private val authRepository: AuthRepository = mockk()

    @Before
    fun setUp() {
        registerUseCase = RegisterUseCase(authRepository)
    }

    private fun validCredentials() = RegisterCredentials(
        username = Username.create("testuser").getOrThrow(),
        email = Email.create("test@example.com").getOrThrow(),
        password = Password.create("password123").getOrThrow()
    )

    @Test
    fun `given valid credentials, when register, then repository is called once`() = runTest {
        // Given
        val credentials = validCredentials()
        val expectedUser = RegisteredUser(id = 1, username = "testuser", email = "test@example.com")
        coEvery { authRepository.register(credentials) } returns Result.success(expectedUser)

        // When
        registerUseCase(credentials)

        // Then
        coVerify(exactly = 1) { authRepository.register(credentials) }
    }

    @Test
    fun `given valid credentials, when register, then returns success with registered user`() = runTest {
        // Given
        val credentials = validCredentials()
        val expectedUser = RegisteredUser(id = 1, username = "testuser", email = "test@example.com")
        coEvery { authRepository.register(credentials) } returns Result.success(expectedUser)

        // When
        val result = registerUseCase(credentials)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
    }

    @Test
    fun `given repository fails, when register, then returns failure`() = runTest {
        // Given
        val credentials = validCredentials()
        coEvery { authRepository.register(credentials) } returns Result.failure(Exception("Email already in use"))

        // When
        val result = registerUseCase(credentials)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Email already in use", result.exceptionOrNull()?.message)
    }

}