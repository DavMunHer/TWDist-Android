package com.example.twdist_android.usecase.auth

import com.example.twdist_android.features.auth.domain.model.LoginCredentials
import com.example.twdist_android.features.auth.domain.model.shared.Email
import com.example.twdist_android.features.auth.domain.model.shared.Password
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import com.example.twdist_android.features.auth.application.usecases.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test


class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private val authRepository: AuthRepository = mockk()

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(authRepository)
    }
    @Test
    fun `given valid credentials, when login, then repository sendLogin is called once`() = runTest {
        // Given
        val emailResult = Email.create("user@email.com")
        val passwordResult = Password.create("password123")
        val credentials = LoginCredentials(emailResult.getOrThrow(), passwordResult.getOrThrow())
        coEvery { authRepository.sendLogin(credentials) } returns Unit

        // When
        loginUseCase(credentials)

        // Then
        coVerify(exactly = 1) { authRepository.sendLogin(credentials) }
    }

    @Test
    fun `given repository throws, when login, then exception is propagated`() = runTest {
        // Given
        val emailResult = Email.create("user@email.com")
        val passwordResult = Password.create("thisIsWrongPassword")
        val credentials = LoginCredentials(emailResult.getOrThrow(), passwordResult.getOrThrow())
        coEvery { authRepository.sendLogin(credentials) } throws Exception("Unauthorized")

        // When / Then
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.runBlocking { loginUseCase(credentials) }
        }
    }

}