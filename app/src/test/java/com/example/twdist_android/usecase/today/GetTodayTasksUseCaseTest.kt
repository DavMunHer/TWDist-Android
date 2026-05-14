package com.example.twdist_android.usecase.today

import com.example.twdist_android.features.today.application.usecases.GetTodayTasksUseCase
import com.example.twdist_android.features.today.domain.model.TodayTask
import com.example.twdist_android.features.today.domain.repository.TodayRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GetTodayTasksUseCaseTest {
    private lateinit var getTodayTasksUseCase: GetTodayTasksUseCase
    private val todayRepository: TodayRepository = mockk()

    @Before
    fun setUp() {
        getTodayTasksUseCase = GetTodayTasksUseCase(todayRepository)
    }

    @Test
    fun `delegates to repository observeTodayTasks`() = runTest {
        val today = LocalDate.of(2026, 5, 14)
        val tasks = listOf(
            TodayTask(id = 1L, sectionId = 2L, name = "A", projectId = 3L, projectName = "P")
        )
        every { todayRepository.observeTodayTasks(today) } returns flowOf(tasks)

        val result = getTodayTasksUseCase(today).first()

        assertEquals(tasks, result)
        verify(exactly = 1) { todayRepository.observeTodayTasks(today) }
    }
}
