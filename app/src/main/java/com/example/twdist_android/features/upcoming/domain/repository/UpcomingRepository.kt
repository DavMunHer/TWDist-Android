package com.example.twdist_android.features.upcoming.domain.repository

import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface UpcomingRepository {
    fun observeUpcomingTasks(from: LocalDate, to: LocalDate): Flow<List<UpcomingTask>>
    suspend fun refreshUpcomingTasks(from: LocalDate, to: LocalDate): Result<Unit>
    suspend fun completeTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
    suspend fun undoCompleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
}
