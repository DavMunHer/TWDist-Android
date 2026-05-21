package com.example.twdist_android.features.upcoming.presentation.viewmodel

import com.example.twdist_android.features.upcoming.application.usecases.CompleteUpcomingTaskUseCase
import com.example.twdist_android.features.upcoming.application.usecases.GetUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.application.usecases.RefreshUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.application.usecases.UndoCompleteUpcomingTaskUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
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
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class UpcomingViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getUpcomingTasksUseCase: GetUpcomingTasksUseCase = mockk()
    private val refreshUpcomingTasksUseCase: RefreshUpcomingTasksUseCase = mockk()
    private val completeUpcomingTaskUseCase: CompleteUpcomingTaskUseCase = mockk()
    private val undoCompleteUpcomingTaskUseCase: UndoCompleteUpcomingTaskUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getUpcomingTasksUseCase(any(), any()) } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresh uses rolling one month window from today`() = runTest {
        val fromSlot = slot<LocalDate>()
        val toSlot = slot<LocalDate>()
        coEvery { refreshUpcomingTasksUseCase(capture(fromSlot), capture(toSlot)) } returns Result.success(Unit)

        UpcomingViewModel(
            getUpcomingTasksUseCase,
            refreshUpcomingTasksUseCase,
            completeUpcomingTaskUseCase,
            undoCompleteUpcomingTaskUseCase
        )
        advanceUntilIdle()

        val today = LocalDate.now()
        assertEquals(today, fromSlot.captured)
        assertEquals(today.plusMonths(1), toSlot.captured)
    }

    @Test
    fun `init triggers refresh upcoming range`() = runTest {
        coEvery { refreshUpcomingTasksUseCase(any(), any()) } returns Result.success(Unit)

        UpcomingViewModel(
            getUpcomingTasksUseCase,
            refreshUpcomingTasksUseCase,
            completeUpcomingTaskUseCase,
            undoCompleteUpcomingTaskUseCase
        )
        advanceUntilIdle()

        coVerify(atLeast = 1) { refreshUpcomingTasksUseCase(any(), any()) }
    }

    @Test
    fun `init on refresh failure sets error`() = runTest {
        coEvery { refreshUpcomingTasksUseCase(any(), any()) } returns Result.failure(
            IllegalStateException("timeout")
        )

        val vm = UpcomingViewModel(
            getUpcomingTasksUseCase,
            refreshUpcomingTasksUseCase,
            completeUpcomingTaskUseCase,
            undoCompleteUpcomingTaskUseCase
        )
        advanceUntilIdle()

        assertEquals("timeout", vm.uiState.value.error)
    }

    @Test
    fun `onVisibleDateChanged updates visible date and week start`() = runTest {
        coEvery { refreshUpcomingTasksUseCase(any(), any()) } returns Result.success(Unit)

        val vm = UpcomingViewModel(
            getUpcomingTasksUseCase,
            refreshUpcomingTasksUseCase,
            completeUpcomingTaskUseCase,
            undoCompleteUpcomingTaskUseCase
        )
        advanceUntilIdle()

        val date = LocalDate.of(2026, 6, 10)
        vm.onVisibleDateChanged(date)

        assertEquals(date, vm.uiState.value.visibleDate)
    }
}
