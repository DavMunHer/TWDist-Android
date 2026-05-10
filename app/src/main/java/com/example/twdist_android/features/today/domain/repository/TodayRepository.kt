package com.example.twdist_android.features.today.domain.repository

import com.example.twdist_android.features.today.domain.model.TodayTask
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TodayRepository {
    fun observeTodayTasks(today: LocalDate): Flow<List<TodayTask>>
    suspend fun refreshTodayTasks(): Result<Unit>
    suspend fun completeTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
    suspend fun undoCompleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
}
