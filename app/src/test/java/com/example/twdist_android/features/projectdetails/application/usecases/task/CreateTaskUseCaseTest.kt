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

class CreateTaskUseCaseTest {
    private lateinit var useCase: CreateTaskUseCase
    private val repository: TaskRepository = mockk()

    @Before
    fun setUp() {
        useCase = CreateTaskUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        val name = TaskName.create("New task").getOrThrow()
        val task = Task(id = 9L, sectionId = 2L, name = "New task", completed = false)
        coEvery { repository.createTask(1L, 2L, name) } returns Result.success(task)

        val result = useCase(1L, 2L, name)

        coVerify(exactly = 1) { repository.createTask(1L, 2L, name) }
        assertTrue(result.isSuccess)
        assertEquals(task, result.getOrThrow())
    }
}
