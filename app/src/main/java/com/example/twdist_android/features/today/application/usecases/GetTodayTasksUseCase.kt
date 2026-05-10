package com.example.twdist_android.features.today.application.usecases

import com.example.twdist_android.features.today.domain.model.TodayTask
import com.example.twdist_android.features.today.domain.repository.TodayRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTodayTasksUseCase @Inject constructor(
    private val repository: TodayRepository
) {
    operator fun invoke(today: LocalDate = LocalDate.now()): Flow<List<TodayTask>> =
        repository.observeTodayTasks(today)
}
