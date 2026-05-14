package com.example.twdist_android.features.taskdetails.presentation.viewmodel

import com.example.twdist_android.features.projectdetails.application.usecases.project.GetProjectByIdUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.CompleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.DeleteTaskUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.GetTasksBySectionUseCase
import com.example.twdist_android.features.projectdetails.application.usecases.task.UpdateTaskUseCase
import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.taskdetails.presentation.event.TaskDetailsEvent
import io.mockk.coEvery
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getProjectByIdUseCase: GetProjectByIdUseCase = mockk()
    private val getTasksBySectionUseCase: GetTasksBySectionUseCase = mockk()
    private val updateTaskUseCase: UpdateTaskUseCase = mockk()
    private val completeTaskUseCase: CompleteTaskUseCase = mockk()
    private val deleteTaskUseCase: DeleteTaskUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTaskDetails populates task and project name`() = runTest {
        val aggregate = sampleAggregate()
        coEvery { getProjectByIdUseCase(1L) } returns Result.success(aggregate)
        coEvery { getTasksBySectionUseCase(1L, 10L) } returns Result.success(
            listOf(Task(id = 100L, sectionId = 10L, name = "Do it", completed = false))
        )

        val vm = TaskDetailsViewModel(
            getProjectByIdUseCase,
            getTasksBySectionUseCase,
            updateTaskUseCase,
            completeTaskUseCase,
            deleteTaskUseCase
        )
        vm.loadTaskDetails(1L, 10L, 100L)
        advanceUntilIdle()

        assertEquals("Inbox", vm.uiState.value.projectName)
        assertEquals("Do it", vm.uiState.value.task?.name)
        assertEquals(false, vm.uiState.value.isLoading)
    }

    @Test
    fun `menu opened updates state`() = runTest {
        val vm = TaskDetailsViewModel(
            getProjectByIdUseCase,
            getTasksBySectionUseCase,
            updateTaskUseCase,
            completeTaskUseCase,
            deleteTaskUseCase
        )
        vm.onEvent(TaskDetailsEvent.MenuOpened)
        assertTrue(vm.uiState.value.openProjectMenu)
    }

    private fun sampleAggregate(): ProjectAggregate {
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
            taskIds = emptyList()
        ).getOrThrow()
        return ProjectAggregate(project = project, sections = listOf(section))
    }
}
