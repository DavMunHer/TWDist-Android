package com.example.twdist_android.features.projectdetails.application.usecases.task

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateTaskUseCaseTest {
    private lateinit var useCase: UpdateTaskUseCase
    private val repository: TaskRepository = mockk()

    @Before
    fun setUp() {
        useCase = UpdateTaskUseCase(repository)
    }

    @Test
    fun `delegates to repository with optional fields`() = runTest {
        val name = TaskName.create("Updated").getOrThrow()
        val task = Task(id = 5L, sectionId = 2L, name = "Updated", completed = false, description = "d")
        coEvery {
            repository.updateTask(1L, 2L, 5L, name, "d", "2026-05-01", null)
        } returns Result.success(task)

        val result = useCase(1L, 2L, 5L, name, description = "d", startDate = "2026-05-01", endDate = null)

        coVerify(exactly = 1) {
            repository.updateTask(1L, 2L, 5L, name, "d", "2026-05-01", null)
        }
        assertTrue(result.isSuccess)
        assertEquals(task, result.getOrThrow())
    }
}
