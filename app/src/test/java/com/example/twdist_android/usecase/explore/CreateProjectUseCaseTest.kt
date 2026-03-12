package com.example.twdist_android.usecase.explore

import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository
import com.example.twdist_android.features.explore.domain.usecases.CreateProjectUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateProjectUseCaseTest {
    private lateinit var createProjectUseCase: CreateProjectUseCase

    private val exploreRepository: ExploreRepository = mockk()

    @Before
    fun setUp() {
        createProjectUseCase = CreateProjectUseCase(exploreRepository)
    }

    @Test
    fun `given valid ProjectName repository createProject is called once`() = runTest {
        // Given
        val projectName = ProjectName.create("Valid Project Name").getOrThrow()
        coEvery { exploreRepository.createProject(projectName) } returns Result.success(Unit)

        // When
        val result = createProjectUseCase(projectName)

        // Then
        coVerify(exactly = 1) { exploreRepository.createProject(projectName) }
        assert(result.isSuccess)
    }

    @Test
    fun `given repository failure should return failure result`() = runTest {
        // Given
        val projectName = ProjectName.create("Valid Project Name").getOrThrow()
        val expectedError = Exception("Network error")
        coEvery { exploreRepository.createProject(projectName) } returns Result.failure(expectedError)

        // When
        val result = createProjectUseCase(projectName)

        // Then
        coVerify(exactly = 1) { exploreRepository.createProject(projectName) }
        assert(result.isFailure)
        assert(result.exceptionOrNull() == expectedError)
    }
}