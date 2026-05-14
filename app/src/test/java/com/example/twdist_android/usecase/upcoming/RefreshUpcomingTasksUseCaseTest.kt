package com.example.twdist_android.usecase.upcoming

import com.example.twdist_android.features.upcoming.application.usecases.RefreshUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class RefreshUpcomingTasksUseCaseTest {
    private lateinit var refreshUpcomingTasksUseCase: RefreshUpcomingTasksUseCase
    private val upcomingRepository: UpcomingRepository = mockk()

    @Before
    fun setUp() {
        refreshUpcomingTasksUseCase = RefreshUpcomingTasksUseCase(upcomingRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val from = LocalDate.of(2026, 5, 1)
        val to = LocalDate.of(2026, 5, 31)
        coEvery { upcomingRepository.refreshUpcomingTasks(from, to) } returns Result.success(Unit)

        val result = refreshUpcomingTasksUseCase(from, to)

        coVerify(exactly = 1) { upcomingRepository.refreshUpcomingTasks(from, to) }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val from = LocalDate.of(2026, 6, 1)
        val to = LocalDate.of(2026, 6, 30)
        val expectedError = IllegalStateException("timeout")
        coEvery { upcomingRepository.refreshUpcomingTasks(from, to) } returns Result.failure(expectedError)

        val result = refreshUpcomingTasksUseCase(from, to)

        coVerify(exactly = 1) { upcomingRepository.refreshUpcomingTasks(from, to) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
