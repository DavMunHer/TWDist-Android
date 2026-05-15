package com.example.twdist_android.features.auth.application.usecases

import com.example.twdist_android.features.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RefreshSessionUseCaseTest {
    private val authRepository: AuthRepository = mockk()
    private lateinit var refreshSessionUseCase: RefreshSessionUseCase

    @Before
    fun setUp() {
        refreshSessionUseCase = RefreshSessionUseCase(authRepository)
    }

    @Test
    fun `invoke delegates to auth repository refreshSession`() = runTest {
        coEvery { authRepository.refreshSession() } returns Result.success(Unit)

        val result = refreshSessionUseCase()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.refreshSession() }
    }
}
