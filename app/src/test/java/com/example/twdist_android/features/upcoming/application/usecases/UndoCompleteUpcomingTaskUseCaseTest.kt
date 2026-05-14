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

class UndoCompleteUpcomingTaskUseCaseTest {
    private lateinit var undoCompleteUpcomingTaskUseCase: UndoCompleteUpcomingTaskUseCase
    private val upcomingRepository: UpcomingRepository = mockk()

    @Before
    fun setUp() {
        undoCompleteUpcomingTaskUseCase = UndoCompleteUpcomingTaskUseCase(upcomingRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val projectId = 5L
        val sectionId = 6L
        val taskId = 7L
        coEvery {
            upcomingRepository.undoCompleteTask(projectId, sectionId, taskId)
        } returns Result.success(Unit)

        val result = undoCompleteUpcomingTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { upcomingRepository.undoCompleteTask(projectId, sectionId, taskId) }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val projectId = 5L
        val sectionId = 6L
        val taskId = 7L
        val expectedError = IllegalStateException("stale")
        coEvery {
            upcomingRepository.undoCompleteTask(projectId, sectionId, taskId)
        } returns Result.failure(expectedError)

        val result = undoCompleteUpcomingTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { upcomingRepository.undoCompleteTask(projectId, sectionId, taskId) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
