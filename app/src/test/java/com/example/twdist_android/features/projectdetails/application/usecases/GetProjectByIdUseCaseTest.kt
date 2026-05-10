package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.application.usecases.project.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProjectByIdUseCaseTest {
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    private val projectDetailsRepository: ProjectDetailsRepository = mockk()

    @Before
    fun setUp() {
        getProjectByIdUseCase = GetProjectByIdUseCase(projectDetailsRepository)
    }

    @Test
    fun `given repository success aggregate is returned`() = runTest {
        val projectName = ProjectName.create("Inbox").getOrThrow()
        val sectionName = SectionName.create("Backlog").getOrThrow()

        val project = Project.create(
            id = 10L,
            name = projectName,
            isFavorite = true,
            sectionIds = listOf(100L)
        ).getOrThrow()
        val section = Section.create(
            id = 100L,
            projectId = 10L,
            name = sectionName,
            taskIds = listOf("task-1")
        ).getOrThrow()
        val aggregate = ProjectAggregate(project = project, sections = listOf(section))

        coEvery { projectDetailsRepository.getProjectById(10L) } returns Result.success(aggregate)

        val result = getProjectByIdUseCase(10L)

        coVerify(exactly = 1) { projectDetailsRepository.getProjectById(10L) }
        assertTrue(result.isSuccess)
        assertEquals(aggregate, result.getOrThrow())
    }

    @Test
    fun `given repository failure failure is returned`() = runTest {
        val expectedError = IllegalArgumentException("not found")
        coEvery { projectDetailsRepository.getProjectById(404L) } returns Result.failure(expectedError)

        val result = getProjectByIdUseCase(404L)

        coVerify(exactly = 1) { projectDetailsRepository.getProjectById(404L) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
