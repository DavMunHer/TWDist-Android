package com.example.twdist_android.features.projectdetails.application.usecases

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProjectByIdUseCaseTest {
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    private val projectDetailsRepository: ProjectDetailsRepository = mockk()
    private val sectionStateStore: SectionStateStore = mockk(relaxed = true)

    @Before
    fun setUp() {
        getProjectByIdUseCase = GetProjectByIdUseCase(projectDetailsRepository, sectionStateStore)
    }

    @Test
    fun `given repository success sections are cached and aggregate is returned`() = runTest {
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
        verify(exactly = 1) { sectionStateStore.upsertAll(listOf(section)) }
        assertTrue(result.isSuccess)
        assertEquals(aggregate, result.getOrThrow())
    }

    @Test
    fun `given repository failure sections are not cached and failure is returned`() = runTest {
        val expectedError = IllegalArgumentException("not found")
        coEvery { projectDetailsRepository.getProjectById(404L) } returns Result.failure(expectedError)

        val result = getProjectByIdUseCase(404L)

        coVerify(exactly = 1) { projectDetailsRepository.getProjectById(404L) }
        verify(exactly = 0) { sectionStateStore.upsertAll(any()) }
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}
