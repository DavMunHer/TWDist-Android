package com.example.twdist_android.features.projectdetails.presentation.viewmodel

import com.example.twdist_android.features.projectdetails.application.usecases.DeleteSectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.DeleteProjectUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.CreateTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.GetTasksBySectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateProjectNameUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateSectionNameUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.UpdateTaskUseCase
import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.model.TaskName
import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.repository.ProjectDetailsRepository
import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import com.example.twdist_android.features.projectdetails.domain.repository.TaskRepository
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
import com.example.twdist_android.features.projectdetails.presentation.event.ProjectEvent
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
    private val taskRepository: TaskRepository = mockk()

    private lateinit var viewModel: ProjectDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val getProjectByIdUseCase = GetProjectByIdUseCase(projectDetailsRepository, sectionStateStore)
        val updateProjectNameUseCase = UpdateProjectNameUseCase(projectDetailsRepository)
        val deleteProjectUseCase = DeleteProjectUseCase(projectDetailsRepository)
        val updateSectionNameUseCase = UpdateSectionNameUseCase(sectionRepository)
        val deleteSectionUseCase = DeleteSectionUseCase(sectionRepository)
        val getTasksBySectionUseCase = GetTasksBySectionUseCase(taskRepository)
        val createTaskUseCase = CreateTaskUseCase(taskRepository)
        val updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        val deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
        coEvery { taskRepository.getTasksBySection(any(), any()) } returns Result.success(
            listOf(
                Task(
                    id = 100L,
                    sectionId = 10L,
                    name = "task-1",
                    completed = false
                )
            )
        )
        viewModel = ProjectDetailsViewModel(
            getProjectByIdUseCase = getProjectByIdUseCase,
            updateProjectNameUseCase = updateProjectNameUseCase,
            deleteProjectUseCase = deleteProjectUseCase,
            updateSectionNameUseCase = updateSectionNameUseCase,
            deleteSectionUseCase = deleteSectionUseCase,
            getTasksBySectionUseCase = getTasksBySectionUseCase,
            createTaskUseCase = createTaskUseCase,
            updateTaskUseCase = updateTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase
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

    @Test
    fun `onEditProjectConfirmed with invalid name should keep dialog open and show error`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()

        viewModel.onProjectEvent(ProjectEvent.EditClicked)
        viewModel.onProjectEvent(ProjectEvent.NameChanged("a"))
        viewModel.onProjectEvent(ProjectEvent.EditConfirmed)

        val state = viewModel.uiState.value
        assertEquals(true, state.isEditingProject)
        assertEquals("Project name must be at least 2 characters", state.projectActionError)
    }

    @Test
    fun `onEditProjectConfirmed success should close dialog and update name optimistically`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)
        coEvery { projectDetailsRepository.updateProjectName(1L, any()) } returns Result.success(Unit)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()

        viewModel.onProjectEvent(ProjectEvent.EditClicked)
        viewModel.onProjectEvent(ProjectEvent.NameChanged("Updated"))
        viewModel.onProjectEvent(ProjectEvent.EditConfirmed)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isEditingProject)
        assertEquals("", state.editingProjectName)
        assertNull(state.projectActionError)
        assertEquals("Updated", state.project?.name)
        coVerify(exactly = 1) { projectDetailsRepository.updateProjectName(1L, any()) }
    }

    @Test
    fun `onEditProjectConfirmed API failure should rollback name and show error`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)
        coEvery {
            projectDetailsRepository.updateProjectName(1L, any())
        } returns Result.failure(IllegalStateException("backend error"))

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()

        viewModel.onProjectEvent(ProjectEvent.EditClicked)
        viewModel.onProjectEvent(ProjectEvent.NameChanged("Updated"))
        viewModel.onProjectEvent(ProjectEvent.EditConfirmed)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Inbox", state.project?.name)
        assertEquals("Could not update project", state.projectActionError)
    }

    @Test
    fun `onDeleteProjectConfirmed success should mark project deleted`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)
        coEvery { projectDetailsRepository.deleteProject(1L) } returns Result.success(Unit)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()

        viewModel.onProjectEvent(ProjectEvent.DeleteClicked)
        viewModel.onProjectEvent(ProjectEvent.DeleteConfirmed)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(true, state.projectDeleted)
        assertNull(state.projectActionError)
        coVerify(exactly = 1) { projectDetailsRepository.deleteProject(1L) }
    }

    @Test
    fun `onProjectDeletedHandled should reset deleted flag`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)
        coEvery { projectDetailsRepository.deleteProject(1L) } returns Result.success(Unit)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()
        viewModel.onProjectEvent(ProjectEvent.DeleteClicked)
        viewModel.onProjectEvent(ProjectEvent.DeleteConfirmed)
        advanceUntilIdle()

        viewModel.onProjectEvent(ProjectEvent.DeletedHandled)
        assertEquals(false, viewModel.uiState.value.projectDeleted)
    }

    @Test
    fun `create task should append task to matching section`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)
        coEvery { taskRepository.createTask(1L, 10L, TaskName.create("Task2").getOrThrow()) } returns Result.success(
            Task(id = 101L, sectionId = 10L, name = "Task2", completed = false)
        )

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()
        viewModel.onEvent(SectionEvent.AddTaskClicked(10L))
        viewModel.onEvent(SectionEvent.CreateTaskNameChanged("Task2"))
        viewModel.onEvent(SectionEvent.CreateTaskConfirmed)
        advanceUntilIdle()

        val taskNames = viewModel.uiState.value.project?.sections?.firstOrNull { it.id == 10L }
            ?.tasks
            ?.map { it.name }
        assertEquals(listOf("task-1", "Task2"), taskNames)
    }

    @Test
    fun `task completion toggle should update local state only`() = runTest {
        val aggregate = createAggregate()
        coEvery { projectDetailsRepository.getProjectById(1L) } returns Result.success(aggregate)

        viewModel.loadProjectDetails(1L)
        advanceUntilIdle()
        viewModel.onEvent(SectionEvent.TaskCompletionToggled(sectionId = 10L, taskId = 100L))

        val task = viewModel.uiState.value.project?.sections?.firstOrNull { it.id == 10L }
            ?.tasks
            ?.firstOrNull { it.id == 100L }
        assertEquals(true, task?.completed)
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
