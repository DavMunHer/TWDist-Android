package com.example.twdist_android.usecase.explore

import com.example.twdist_android.features.explore.application.usecases.DeleteProjectUseCase
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteProjectUseCaseTest {
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private val projectRepository: ProjectRepository = mockk()

    @Before
    fun setUp() {
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val projectId = 42L
        coEvery { projectRepository.deleteProject(projectId) } returns Result.success(Unit)

        val result = deleteProjectUseCase(projectId)

        coVerify(exactly = 1) { projectRepository.deleteProject(projectId) }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val projectId = 7L
        val expectedError = IllegalStateException("boom")
        coEvery { projectRepository.deleteProject(projectId) } returns Result.failure(expectedError)

        val result = deleteProjectUseCase(projectId)

        coVerify(exactly = 1) { projectRepository.deleteProject(projectId) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
