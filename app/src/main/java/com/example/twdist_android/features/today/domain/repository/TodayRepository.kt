package com.example.twdist_android.features.today.domain.repository

import com.example.twdist_android.features.today.domain.model.TodayTask

interface TodayRepository {
    suspend fun getTodayTasks(): Result<List<TodayTask>>
    suspend fun completeTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
    suspend fun undoCompleteTask(projectId: Long, sectionId: Long, taskId: Long): Result<Unit>
}
