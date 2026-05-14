package com.example.twdist_android.features.upcoming.application.usecases

import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CompleteUpcomingTaskUseCaseTest {
    private lateinit var completeUpcomingTaskUseCase: CompleteUpcomingTaskUseCase
    private val upcomingRepository: UpcomingRepository = mockk()

    @Before
    fun setUp() {
        completeUpcomingTaskUseCase = CompleteUpcomingTaskUseCase(upcomingRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val projectId = 1L
        val sectionId = 2L
        val taskId = 3L
        coEvery {
            upcomingRepository.completeTask(projectId, sectionId, taskId)
        } returns Result.success(Unit)

        val result = completeUpcomingTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { upcomingRepository.completeTask(projectId, sectionId, taskId) }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val projectId = 1L
        val sectionId = 2L
        val taskId = 3L
        val expectedError = IllegalStateException("gone")
        coEvery {
            upcomingRepository.completeTask(projectId, sectionId, taskId)
        } returns Result.failure(expectedError)

        val result = completeUpcomingTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { upcomingRepository.completeTask(projectId, sectionId, taskId) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
