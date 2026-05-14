package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateProjectUseCaseTest {
    private lateinit var createProjectUseCase: CreateProjectUseCase

    private val projectRepository: ProjectRepository = mockk()

    @Before
    fun setUp() {
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }

    @Test
    fun `given valid ProjectName repository createProject is called once`() = runTest {
        val projectName = ProjectName.create("Valid Project Name").getOrThrow()
        val project = Project.create(id = 1L, name = projectName).getOrThrow()
        coEvery { projectRepository.createProject(projectName) } returns Result.success(project)

        val result = createProjectUseCase(projectName)

        coVerify(exactly = 1) { projectRepository.createProject(projectName) }
        assert(result.isSuccess)
    }

    @Test
    fun `given repository failure should return failure result`() = runTest {
        val projectName = ProjectName.create("Valid Project Name").getOrThrow()
        val expectedError = Exception("Network error")
        coEvery { projectRepository.createProject(projectName) } returns Result.failure(expectedError)

        val result = createProjectUseCase(projectName)

        coVerify(exactly = 1) { projectRepository.createProject(projectName) }
        assert(result.isFailure)
        assert(result.exceptionOrNull() == expectedError)
    }
}
