package com.example.twdist_android.features.projectdetails.application.usecases.project

import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteProjectUseCaseTest {
    private lateinit var useCase: DeleteProjectUseCase
    private val repository: ProjectDetailsRepository = mockk()

    @Before
    fun setUp() {
        useCase = DeleteProjectUseCase(repository)
    }

    @Test
    fun `delegates to project details repository`() = runTest {
        coEvery { repository.deleteProject(99L) } returns Result.success(Unit)

        val result = useCase(99L)

        coVerify(exactly = 1) { repository.deleteProject(99L) }
        assertTrue(result.isSuccess)
    }
}
