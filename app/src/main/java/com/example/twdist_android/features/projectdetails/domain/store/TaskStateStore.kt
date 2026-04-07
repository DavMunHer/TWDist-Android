package com.example.twdist_android.features.projectdetails.domain.store

import com.example.twdist_android.features.projectdetails.domain.model.Task

interface TaskStateStore {
    fun upsert(task: Task)
    fun upsertAll(tasks: List<Task>)
    fun getById(taskId: Long): Task?
    fun getBySectionId(sectionId: Long): List<Task>
    fun remove(taskId: Long)
    fun removeBySectionId(sectionId: Long)
}
