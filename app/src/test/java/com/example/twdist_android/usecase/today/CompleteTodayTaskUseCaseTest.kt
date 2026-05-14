package com.example.twdist_android.usecase.today

import com.example.twdist_android.features.today.application.usecases.CompleteTodayTaskUseCase
import com.example.twdist_android.features.today.domain.repository.TodayRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CompleteTodayTaskUseCaseTest {
    private lateinit var completeTodayTaskUseCase: CompleteTodayTaskUseCase
    private val todayRepository: TodayRepository = mockk()

    @Before
    fun setUp() {
        completeTodayTaskUseCase = CompleteTodayTaskUseCase(todayRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val projectId = 1L
        val sectionId = 2L
        val taskId = 3L
        coEvery {
            todayRepository.completeTask(projectId, sectionId, taskId)
        } returns Result.success(Unit)

        val result = completeTodayTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { todayRepository.completeTask(projectId, sectionId, taskId) }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val projectId = 1L
        val sectionId = 2L
        val taskId = 3L
        val expectedError = IllegalStateException("offline")
        coEvery {
            todayRepository.completeTask(projectId, sectionId, taskId)
        } returns Result.failure(expectedError)

        val result = completeTodayTaskUseCase(projectId, sectionId, taskId)

        coVerify(exactly = 1) { todayRepository.completeTask(projectId, sectionId, taskId) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
