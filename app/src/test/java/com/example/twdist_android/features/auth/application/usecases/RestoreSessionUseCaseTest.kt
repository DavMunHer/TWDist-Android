package com.example.twdist_android.features.auth.application.usecases

import com.example.twdist_android.features.auth.domain.model.RegisteredUser
import com.example.twdist_android.features.auth.domain.model.SessionStatus
import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestoreSessionUseCaseTest {
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val authSessionManager = AuthSessionManager()
    private lateinit var restoreSessionUseCase: RestoreSessionUseCase

    private val user = RegisteredUser(id = 1L, username = "test", email = "user@email.com")

    @Before
    fun setUp() {
        restoreSessionUseCase = RestoreSessionUseCase(authRepository, authSessionManager)
    }

    @Test
    fun `when getCurrentUser succeeds then session is authenticated`() = runTest {
        coEvery { authRepository.getCurrentUser() } returns Result.success(user)

        restoreSessionUseCase()

        assertEquals(SessionStatus.Authenticated(user), authSessionManager.sessionStatus.value)
        coVerify(exactly = 0) { authRepository.refreshSession() }
    }

    @Test
    fun `when getCurrentUser fails and refresh succeeds then session is authenticated`() = runTest {
        coEvery { authRepository.getCurrentUser() } returnsMany listOf(
            Result.failure(Exception("401")),
            Result.success(user)
        )
        coEvery { authRepository.refreshSession() } returns Result.success(Unit)

        restoreSessionUseCase()

        assertEquals(SessionStatus.Authenticated(user), authSessionManager.sessionStatus.value)
        coVerify(exactly = 1) { authRepository.refreshSession() }
    }

    @Test
    fun `when getCurrentUser and refresh fail then session is unauthenticated and cookies cleared`() =
        runTest {
            coEvery { authRepository.getCurrentUser() } returns Result.failure(Exception("401"))
            coEvery { authRepository.refreshSession() } returns Result.failure(Exception("401"))
            coEvery { authRepository.clearLocalSession() } returns Unit

            restoreSessionUseCase()

            assertEquals(SessionStatus.Unauthenticated, authSessionManager.sessionStatus.value)
            coVerify(exactly = 1) { authRepository.clearLocalSession() }
        }
}
