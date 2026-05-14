package com.example.twdist_android.usecase.upcoming

import com.example.twdist_android.features.upcoming.application.usecases.GetUpcomingTasksUseCase
import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
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

class GetUpcomingTasksUseCaseTest {
    private lateinit var getUpcomingTasksUseCase: GetUpcomingTasksUseCase
    private val upcomingRepository: UpcomingRepository = mockk()

    @Before
    fun setUp() {
        getUpcomingTasksUseCase = GetUpcomingTasksUseCase(upcomingRepository)
    }

    @Test
    fun `delegates to repository observeUpcomingTasks`() = runTest {
        val from = LocalDate.of(2026, 5, 1)
        val to = LocalDate.of(2026, 5, 7)
        val tasks = listOf(
            UpcomingTask(
                id = 1L,
                sectionId = 2L,
                projectId = 3L,
                name = "Task",
                projectName = "Proj",
                startDate = from
            )
        )
        every { upcomingRepository.observeUpcomingTasks(from, to) } returns flowOf(tasks)

        val result = getUpcomingTasksUseCase(from, to).first()

        assertEquals(tasks, result)
        verify(exactly = 1) { upcomingRepository.observeUpcomingTasks(from, to) }
    }
}
