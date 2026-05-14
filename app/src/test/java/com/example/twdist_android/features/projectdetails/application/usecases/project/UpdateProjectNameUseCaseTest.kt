package com.example.twdist_android.features.projectdetails.application.usecases.project

import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateProjectNameUseCaseTest {
    private lateinit var useCase: UpdateProjectNameUseCase
    private val repository: ProjectDetailsRepository = mockk()

    @Before
    fun setUp() {
        useCase = UpdateProjectNameUseCase(repository)
    }

    @Test
    fun `passes string name to repository`() = runTest {
        val name = ProjectName.create("Renamed").getOrThrow()
        coEvery { repository.updateProjectName(1L, "Renamed") } returns Result.success(Unit)

        val result = useCase(1L, name)

        coVerify(exactly = 1) { repository.updateProjectName(1L, "Renamed") }
        assertTrue(result.isSuccess)
    }
}
