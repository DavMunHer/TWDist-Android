package com.example.twdist_android.features.today.application.usecases

import com.example.twdist_android.features.today.domain.model.TodayTask
import com.example.twdist_android.features.today.domain.repository.TodayRepository
import javax.inject.Inject

class GetTodayTasksUseCase @Inject constructor(
    private val repository: TodayRepository
) {
    suspend operator fun invoke(): Result<List<TodayTask>> = repository.getTodayTasks()
}
