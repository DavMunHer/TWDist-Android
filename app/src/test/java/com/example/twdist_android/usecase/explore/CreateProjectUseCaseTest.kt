package com.example.twdist_android.usecase.explore

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import com.example.twdist_android.features.explore.application.usecases.CreateProjectUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateProjectUseCaseTest {
    private lateinit var createProjectUseCase: CreateProjectUseCase

    private val projectRepository: ProjectRepository = mockk()
    private val projectStateStore: ProjectStateStore = mockk(relaxed = true)

    @Before
    fun setUp() {
        createProjectUseCase = CreateProjectUseCase(projectRepository, projectStateStore)
    }

    @Test
    fun `given valid ProjectName repository createProject is called once`() = runTest {
        // Given
        val projectName = ProjectName.create("Valid Project Name").getOrThrow()
        val project = Project.create(id = 1L, name = projectName).getOrThrow()
        coEvery { projectRepository.createProject(projectName) } returns Result.success(project)

        // When
        val result = createProjectUseCase(projectName)

        // Then
        coVerify(exactly = 1) { projectRepository.createProject(projectName) }
        assert(result.isSuccess)
    }

    @Test
    fun `given repository failure should return failure result`() = runTest {
        // Given
        val projectName = ProjectName.create("Valid Project Name").getOrThrow()
        val expectedError = Exception("Network error")
        coEvery { projectRepository.createProject(projectName) } returns Result.failure(expectedError)

        // When
        val result = createProjectUseCase(projectName)

        // Then
        coVerify(exactly = 1) { projectRepository.createProject(projectName) }
        assert(result.isFailure)
        assert(result.exceptionOrNull() == expectedError)
    }
}