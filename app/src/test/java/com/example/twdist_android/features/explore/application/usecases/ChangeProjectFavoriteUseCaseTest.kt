package com.example.twdist_android.features.explore.application.usecases

import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChangeProjectFavoriteUseCaseTest {
    private lateinit var changeProjectFavoriteUseCase: ChangeProjectFavoriteUseCase
    private val projectRepository: ProjectRepository = mockk()

    @Before
    fun setUp() {
        changeProjectFavoriteUseCase = ChangeProjectFavoriteUseCase(projectRepository)
    }

    @Test
    fun `given repository success result is returned`() = runTest {
        val projectId = 3L
        coEvery { projectRepository.changeFavorite(projectId, true) } returns Result.success(Unit)

        val result = changeProjectFavoriteUseCase(projectId, true)

        coVerify(exactly = 1) { projectRepository.changeFavorite(projectId, true) }
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val projectId = 9L
        val expectedError = IllegalStateException("network")
        coEvery { projectRepository.changeFavorite(projectId, false) } returns Result.failure(expectedError)

        val result = changeProjectFavoriteUseCase(projectId, false)

        coVerify(exactly = 1) { projectRepository.changeFavorite(projectId, false) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
