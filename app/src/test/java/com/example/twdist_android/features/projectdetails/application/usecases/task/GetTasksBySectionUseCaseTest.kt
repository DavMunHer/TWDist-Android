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

class GetTasksBySectionUseCaseTest {
    private lateinit var useCase: GetTasksBySectionUseCase
    private val repository: TaskRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetTasksBySectionUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        val tasks = listOf(Task(1L, 2L, "A", completed = false))
        coEvery { repository.getTasksBySection(10L, 20L) } returns Result.success(tasks)

        val result = useCase(10L, 20L)

        coVerify(exactly = 1) { repository.getTasksBySection(10L, 20L) }
        assertTrue(result.isSuccess)
        assertEquals(tasks, result.getOrThrow())
    }
}
