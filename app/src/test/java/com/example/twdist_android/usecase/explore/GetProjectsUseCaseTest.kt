package com.example.twdist_android.usecase.explore

import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.application.usecases.GetProjectsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProjectsUseCaseTest {
    private lateinit var getProjectsUseCase: GetProjectsUseCase

    private val projectRepository: ProjectRepository = mockk()

    @Before
    fun setUp() {
        getProjectsUseCase = GetProjectsUseCase(projectRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val name = ProjectName.create("Inbox").getOrThrow()
        val summaries = listOf(
            ProjectSummary(id = 1L, name = name, isFavorite = true, pendingTasks = 4)
        )
        coEvery { projectRepository.getAllProjects() } returns Result.success(summaries)

        val result = getProjectsUseCase()

        coVerify(exactly = 1) { projectRepository.getAllProjects() }
        assertTrue(result.isSuccess)
        assertEquals(summaries, result.getOrThrow())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val expectedError = IllegalStateException("boom")
        coEvery { projectRepository.getAllProjects() } returns Result.failure(expectedError)

        val result = getProjectsUseCase()

        coVerify(exactly = 1) { projectRepository.getAllProjects() }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
