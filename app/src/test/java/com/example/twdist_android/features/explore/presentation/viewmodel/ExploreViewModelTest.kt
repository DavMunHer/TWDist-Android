package com.example.twdist_android.features.explore.presentation.viewmodel

import com.example.twdist_android.features.explore.application.usecases.ChangeProjectFavoriteUseCase
import com.example.twdist_android.features.explore.application.usecases.CreateProjectUseCase
import com.example.twdist_android.features.explore.application.usecases.DeleteProjectUseCase
import com.example.twdist_android.features.explore.application.usecases.GetProjectsUseCase
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.presentation.event.ExploreEvent
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
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
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getProjectsUseCase: GetProjectsUseCase = mockk()
    private val createProjectUseCase: CreateProjectUseCase = mockk()
    private val deleteProjectUseCase: DeleteProjectUseCase = mockk()
    private val changeProjectFavoriteUseCase: ChangeProjectFavoriteUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads projects into state`() = runTest {
        val name = ProjectName.create("Inbox").getOrThrow()
        val summaries = listOf(ProjectSummary(1L, name, isFavorite = false, pendingTasks = 2))
        coEvery { getProjectsUseCase() } returns Result.success(summaries)

        val viewModel = ExploreViewModel(
            getProjectsUseCase,
            createProjectUseCase,
            deleteProjectUseCase,
            changeProjectFavoriteUseCase
        )
        advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.projects.size)
        assertEquals("Inbox", viewModel.uiState.value.projects[0].name)
        coVerify(atLeast = 1) { getProjectsUseCase() }
    }

    @Test
    fun `create project with invalid name sets validation error`() = runTest {
        coEvery { getProjectsUseCase() } returns Result.success(emptyList())

        val viewModel = ExploreViewModel(
            getProjectsUseCase,
            createProjectUseCase,
            deleteProjectUseCase,
            changeProjectFavoriteUseCase
        )
        advanceUntilIdle()

        viewModel.handleEvent(ExploreEvent.CreateProject("a"))
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.projectNameError)
        coVerify(exactly = 0) { createProjectUseCase(any()) }
    }

    @Test
    fun `toggle favorite calls change favorite use case`() = runTest {
        val name = ProjectName.create("Proj").getOrThrow()
        val summaries = listOf(ProjectSummary(5L, name, isFavorite = false, pendingTasks = 0))
        coEvery { getProjectsUseCase() } returns Result.success(summaries)
        coEvery { changeProjectFavoriteUseCase(5L, true) } returns Result.success(Unit)

        val viewModel = ExploreViewModel(
            getProjectsUseCase,
            createProjectUseCase,
            deleteProjectUseCase,
            changeProjectFavoriteUseCase
        )
        advanceUntilIdle()

        viewModel.handleEvent(ExploreEvent.ToggleProjectFavorite(5L))
        advanceUntilIdle()

        coVerify(exactly = 1) { changeProjectFavoriteUseCase(5L, true) }
    }
}
