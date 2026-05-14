package com.example.twdist_android.features.projectdetails.application.usecases.task

import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteTaskUseCaseTest {
    private lateinit var useCase: DeleteTaskUseCase
    private val repository: TaskRepository = mockk()

    @Before
    fun setUp() {
        useCase = DeleteTaskUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        coEvery { repository.deleteTask(1L, 2L, 3L) } returns Result.success(Unit)

        val result = useCase(1L, 2L, 3L)

        coVerify(exactly = 1) { repository.deleteTask(1L, 2L, 3L) }
        assertTrue(result.isSuccess)
    }
}
