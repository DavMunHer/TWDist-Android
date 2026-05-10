package com.example.twdist_android.features.upcoming.application.usecases

import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import java.time.LocalDate
import javax.inject.Inject

class RefreshUpcomingTasksUseCase @Inject constructor(
    private val repository: UpcomingRepository
) {
    suspend operator fun invoke(from: LocalDate, to: LocalDate): Result<Unit> =
        repository.refreshUpcomingTasks(from, to)
}
