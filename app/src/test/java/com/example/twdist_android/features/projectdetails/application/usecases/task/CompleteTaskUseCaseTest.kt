package com.example.twdist_android.features.projectdetails.application.usecases.task

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class CompleteTaskUseCaseTest {
    private lateinit var useCase: CompleteTaskUseCase
    private val repository: TaskRepository = mockk()

    @Before
    fun setUp() {
        useCase = CompleteTaskUseCase(repository)
    }

    @Test
    fun `delegates to repository and returns success`() = runTest {
        val completedDate = LocalDate.of(2026, 5, 14)
        val task = Task(id = 1L, sectionId = 2L, name = "T", completed = true, completedAt = completedDate)
        coEvery { repository.completeTask(10L, 20L, 1L, completedDate) } returns Result.success(task)

        val result = useCase(10L, 20L, 1L, completedDate)

        coVerify(exactly = 1) { repository.completeTask(10L, 20L, 1L, completedDate) }
        assertTrue(result.isSuccess)
        assertEquals(task, result.getOrThrow())
    }

    @Test
    fun `propagates repository failure`() = runTest {
        val err = IllegalStateException("x")
        coEvery { repository.completeTask(any(), any(), any(), any()) } returns Result.failure(err)

        val result = useCase(1L, 2L, 3L, null)

        assertTrue(result.isFailure)
        assertEquals(err, result.exceptionOrNull())
    }
}
