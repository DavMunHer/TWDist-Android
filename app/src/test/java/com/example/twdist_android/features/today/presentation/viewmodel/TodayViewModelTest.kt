package com.example.twdist_android.features.today.presentation.viewmodel

import com.example.twdist_android.features.today.application.usecases.CompleteTodayTaskUseCase
import com.example.twdist_android.features.today.application.usecases.GetTodayTasksUseCase
import com.example.twdist_android.features.today.application.usecases.RefreshTodayTasksUseCase
import com.example.twdist_android.features.today.application.usecases.UndoCompleteTodayTaskUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodayViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getTodayTasksUseCase: GetTodayTasksUseCase = mockk()
    private val refreshTodayTasksUseCase: RefreshTodayTasksUseCase = mockk()
    private val completeTodayTaskUseCase: CompleteTodayTaskUseCase = mockk()
    private val undoCompleteTodayTaskUseCase: UndoCompleteTodayTaskUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getTodayTasksUseCase(any()) } returns flowOf(emptyList())
        coEvery { refreshTodayTasksUseCase() } returns Result.success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init triggers refresh today tasks`() = runTest {
        TodayViewModel(
            getTodayTasksUseCase,
            refreshTodayTasksUseCase,
            completeTodayTaskUseCase,
            undoCompleteTodayTaskUseCase
        )
        advanceUntilIdle()

        coVerify(atLeast = 1) { refreshTodayTasksUseCase() }
    }

    @Test
    fun `loadTodayTasks calls refresh use case`() = runTest {
        val viewModel = TodayViewModel(
            getTodayTasksUseCase,
            refreshTodayTasksUseCase,
            completeTodayTaskUseCase,
            undoCompleteTodayTaskUseCase
        )
        advanceUntilIdle()

        viewModel.loadTodayTasks()
        advanceUntilIdle()

        coVerify(atLeast = 2) { refreshTodayTasksUseCase() }
    }

    @Test
    fun `loadTodayTasks on refresh failure exposes error`() = runTest {
        coEvery { refreshTodayTasksUseCase() } returnsMany listOf(
            Result.success(Unit),
            Result.failure(IllegalStateException("offline"))
        )

        val viewModel = TodayViewModel(
            getTodayTasksUseCase,
            refreshTodayTasksUseCase,
            completeTodayTaskUseCase,
            undoCompleteTodayTaskUseCase
        )
        advanceUntilIdle()

        viewModel.loadTodayTasks()
        advanceUntilIdle()

        assertEquals("offline", viewModel.uiState.value.error)
    }
}
