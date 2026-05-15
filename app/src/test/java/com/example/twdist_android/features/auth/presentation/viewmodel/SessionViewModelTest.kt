package com.example.twdist_android.features.auth.presentation.viewmodel

import com.example.twdist_android.features.auth.application.usecases.RestoreSessionUseCase
import com.example.twdist_android.features.auth.domain.model.RegisteredUser
import com.example.twdist_android.features.auth.domain.model.SessionStatus
import com.example.twdist_android.features.auth.domain.session.AuthSessionManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SessionViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val restoreSessionUseCase: RestoreSessionUseCase = mockk(relaxed = true)
    private val authSessionManager = AuthSessionManager()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init restores session and exposes authenticated status`() = runTest {
        val user = RegisteredUser(id = 1L, username = "test", email = "user@email.com")
        coEvery { restoreSessionUseCase() } coAnswers {
            authSessionManager.setAuthenticated(user)
        }

        val viewModel = SessionViewModel(restoreSessionUseCase, authSessionManager)
        advanceUntilIdle()

        assertEquals(SessionStatus.Authenticated(user), viewModel.sessionStatus.value)
    }

    @Test
    fun `init restores session and exposes unauthenticated status`() = runTest {
        coEvery { restoreSessionUseCase() } coAnswers {
            authSessionManager.setUnauthenticated()
        }

        val viewModel = SessionViewModel(restoreSessionUseCase, authSessionManager)
        advanceUntilIdle()

        assertEquals(SessionStatus.Unauthenticated, viewModel.sessionStatus.value)
    }
}
