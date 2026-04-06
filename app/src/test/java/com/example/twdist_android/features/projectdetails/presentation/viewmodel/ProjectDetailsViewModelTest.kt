package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import com.example.twdist_android.features.projectdetails.application.usecases.DeleteSectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateSectionNameUseCase
import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.presentation.event.SectionEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectDetailsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val projectDetailsRepository: ProjectDetailsRepository = mockk()
    private val sectionStateStore: SectionStateStore = mockk(relaxed = true)
    private val sectionRepository: SectionRepository = mockk()

    private lateinit var viewModel: ProjectDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val getProjectByIdUseCase = GetProjectByIdUseCase(projectDetailsRepository, sectionStateStore)
        val updateSectionNameUseCase = UpdateSectionNameUseCase(sectionRepository)
        val deleteSectionUseCase = DeleteSectionUseCase(sectionRepository)
        viewModel = ProjectDetailsViewModel(
            getProjectByIdUseCase = getProjectByIdUseCase,
            updateSectionNameUseCase = updateSectionNameUseCase,
            deleteSectionUseCase = deleteSectionUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEditSectionClick should open dialog with current section name`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()
        viewModel.onEvent(SectionEvent.EditClicked(10L))

        val state = viewModel.uiState.value
        assertEquals(10L, state.editingSectionId)
        assertEquals("Backlog", state.editingSectionName)
    }

    @Test
    fun `onSaveSectionEdit with invalid name should keep dialog open and show error`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()
        viewModel.onEvent(SectionEvent.EditClicked(10L))
        viewModel.onEvent(SectionEvent.NameChanged("a"))
        viewModel.onEvent(SectionEvent.EditConfirmed)

        val state = viewModel.uiState.value
        assertEquals(10L, state.editingSectionId)
        assertEquals("Section name must be at least 2 characters", state.sectionActionError)
    }

    @Test
    fun `onSaveSectionEdit success should close dialog and update section name optimistically`() = runTest {
        val aggregate = createAggregate()
        val updatedSection = Section.create(
            id = 10L,
            projectId = 1L,
            name = SectionName.create("Updated").getOrThrow(),
            taskIds = listOf("task-1")
        ).getOrThrow()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)
        coEvery { sectionRepository.updateSectionName(10L, any()) } returns Result.success(updatedSection)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()
        viewModel.onEvent(SectionEvent.EditClicked(10L))
        viewModel.onEvent(SectionEvent.NameChanged("Updated"))
        viewModel.onEvent(SectionEvent.EditConfirmed)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.editingSectionId)
        assertEquals("", state.editingSectionName)
        assertNull(state.sectionActionError)
        assertEquals("Updated", state.project?.sections?.firstOrNull { it.id == 10L }?.name)
        coVerify(exactly = 1) { sectionRepository.updateSectionName(10L, any()) }
        coVerify(exactly = 1) { projectDetailsRepository.getProjectById(1L) }
    }

    @Test
    fun `onDeleteSectionConfirm success should clear confirmation and remove section optimistically`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)
        coEvery { sectionRepository.deleteSection(10L) } returns Result.success(Unit)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()
        viewModel.onEvent(SectionEvent.DeleteClicked(10L))
        viewModel.onEvent(SectionEvent.DeleteConfirmed)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.deleteConfirmSectionId)
        assertNull(state.sectionActionError)
        assertEquals(0, state.project?.sections?.size)
        coVerify(exactly = 1) { sectionRepository.deleteSection(10L) }
        coVerify(exactly = 1) { projectDetailsRepository.getProjectById(1L) }
    }

    private fun createAggregate(): ProjectAggregate {
        val project = Project.create(
            id = 1L,
            name = ProjectName.create("Inbox").getOrThrow(),
            isFavorite = false,
            sectionIds = listOf(10L)
        ).getOrThrow()
        val section = Section.create(
            id = 10L,
            projectId = 1L,
            name = SectionName.create("Backlog").getOrThrow(),
            taskIds = listOf("task-1")
        ).getOrThrow()
        return ProjectAggregate(project = project, sections = listOf(section))
    }
}
