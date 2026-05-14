package com.example.twdist_android.features.today.application.usecases

import com.example.twdist_android.features.today.domain.repository.TodayRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RefreshTodayTasksUseCaseTest {
    private lateinit var refreshTodayTasksUseCase: RefreshTodayTasksUseCase
    private val todayRepository: TodayRepository = mockk()

    @Before
    fun setUp() {
        refreshTodayTasksUseCase = RefreshTodayTasksUseCase(todayRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        coEvery { todayRepository.refreshTodayTasks() } returns Result.success(Unit)

        val result = refreshTodayTasksUseCase()

        coVerify(exactly = 1) { todayRepository.refreshTodayTasks() }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val expectedError = IllegalStateException("sync failed")
        coEvery { todayRepository.refreshTodayTasks() } returns Result.failure(expectedError)

        val result = refreshTodayTasksUseCase()

        coVerify(exactly = 1) { todayRepository.refreshTodayTasks() }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
