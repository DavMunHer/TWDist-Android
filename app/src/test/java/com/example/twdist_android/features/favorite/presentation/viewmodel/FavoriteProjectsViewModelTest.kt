package com.example.twdist_android.features.favorite.presentation.viewmodel

import com.example.twdist_android.features.explore.application.usecases.ChangeProjectFavoriteUseCase
import com.example.twdist_android.features.explore.application.usecases.GetProjectsUseCase
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.favorite.presentation.event.FavoriteProjectsEvent
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteProjectsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getProjectsUseCase: GetProjectsUseCase = mockk()
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
    fun `init loads only favorite projects`() = runTest {
        val name = ProjectName.create("Starred").getOrThrow()
        val summaries = listOf(
            ProjectSummary(1L, name, isFavorite = true, pendingTasks = 0),
            ProjectSummary(2L, ProjectName.create("Other").getOrThrow(), isFavorite = false, pendingTasks = 1)
        )
        coEvery { getProjectsUseCase() } returns Result.success(summaries)

        val vm = FavoriteProjectsViewModel(getProjectsUseCase, changeProjectFavoriteUseCase)
        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.projects.size)
        assertEquals(1L, vm.uiState.value.projects[0].id)
    }

    @Test
    fun `unfavorite dispatches change favorite to false`() = runTest {
        val name = ProjectName.create("Starred").getOrThrow()
        val summaries = listOf(ProjectSummary(9L, name, isFavorite = true, pendingTasks = 0))
        coEvery { getProjectsUseCase() } returns Result.success(summaries)
        coEvery { changeProjectFavoriteUseCase(9L, false) } returns Result.success(Unit)

        val vm = FavoriteProjectsViewModel(getProjectsUseCase, changeProjectFavoriteUseCase)
        advanceUntilIdle()

        vm.handleEvent(FavoriteProjectsEvent.UnfavoriteProject(9L))
        advanceUntilIdle()

        coVerify(atLeast = 1) { changeProjectFavoriteUseCase(9L, false) }
        assertTrue(vm.uiState.value.projects.none { it.id == 9L })
    }
}
