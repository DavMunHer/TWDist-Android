package com.example.twdist_android.features.upcoming.domain.repository

import com.example.twdist_android.features.upcoming.domain.model.UpcomingTask
import java.time.LocalDate

interface UpcomingRepository {
    suspend fun getUpcomingTasks(from: LocalDate, to: LocalDate): Result<List<UpcomingTask>>
    suspend fun completeTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
    suspend fun undoCompleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
}
