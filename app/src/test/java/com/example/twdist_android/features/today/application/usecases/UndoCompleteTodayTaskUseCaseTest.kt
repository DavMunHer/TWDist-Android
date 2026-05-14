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

class UndoCompleteTodayTaskUseCaseTest {
    private lateinit var undoCompleteTodayTaskUseCase: UndoCompleteTodayTaskUseCase
    private val todayRepository: TodayRepository = mockk()

    @Before
    fun setUp() {
        undoCompleteTodayTaskUseCase = UndoCompleteTodayTaskUseCase(todayRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val projectId = 10L
        val sectionId = 20L
        val taskId = 30L
        coEvery {
            todayRepository.undoCompleteTask(projectId, sectionId, taskId)
        } returns Result.success(Unit)

        val result = undoCompleteTodayTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { todayRepository.undoCompleteTask(projectId, sectionId, taskId) }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val projectId = 10L
        val sectionId = 20L
        val taskId = 30L
        val expectedError = IllegalStateException("conflict")
        coEvery {
            todayRepository.undoCompleteTask(projectId, sectionId, taskId)
        } returns Result.failure(expectedError)

        val result = undoCompleteTodayTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { todayRepository.undoCompleteTask(projectId, sectionId, taskId) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
