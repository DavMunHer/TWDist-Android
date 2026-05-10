package com.example.twdist_android.features.upcoming.application.usecases

import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import com.example.twdist_android.features.upcoming.domain.repository.UpcomingRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetUpcomingTasksUseCase @Inject constructor(
    private val repository: UpcomingRepository
) {
    operator fun invoke(from: LocalDate, to: LocalDate): Flow<List<UpcomingTask>> =
        repository.observeUpcomingTasks(from, to)
}
