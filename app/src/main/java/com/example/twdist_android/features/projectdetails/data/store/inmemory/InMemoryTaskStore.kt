package com.example.twdist_android.features.projectdetails.data.store.inmemory

import com.example.twdist_android.features.projectdetails.domain.model.Task
import com.example.twdist_android.features.projectdetails.domain.store.TaskStateStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryTaskStore @Inject constructor() : TaskStateStore {
    private val tasksById = linkedMapOf<Long, Task>()
    private val taskIdsBySectionId = linkedMapOf<Long, MutableSet<Long>>()

    private fun upsertInternal(task: Task) {
        val existing = tasksById[task.id]
        if (existing != null && existing.sectionId != task.sectionId) {
            taskIdsBySectionId[existing.sectionId]?.remove(existing.id)
        }
        tasksById[task.id] = task
        taskIdsBySectionId.getOrPut(task.sectionId) { linkedSetOf() }.add(task.id)
    }

    override fun upsert(task: Task) {
        synchronized(this) { upsertInternal(task) }
    }

    override fun upsertAll(tasks: List<Task>) {
        synchronized(this) { tasks.forEach(::upsertInternal) }
    }

    override fun getById(taskId: Long): Task? {
        return synchronized(this) { tasksById[taskId] }
    }

    override fun getBySectionId(sectionId: Long): List<Task> {
        return synchronized(this) {
            taskIdsBySectionId[sectionId].orEmpty().mapNotNull { tasksById[it] }
        }
    }

    override fun remove(taskId: Long) {
        synchronized(this) {
            val removed = tasksById.remove(taskId) ?: return
            taskIdsBySectionId[removed.sectionId]?.remove(taskId)
            if (taskIdsBySectionId[removed.sectionId]?.isEmpty() == true) {
                taskIdsBySectionId.remove(removed.sectionId)
            }
        }
    }

    override fun removeBySectionId(sectionId: Long) {
        synchronized(this) {
            val ids = taskIdsBySectionId.remove(sectionId).orEmpty()
            ids.forEach { tasksById.remove(it) }
        }
    }
}
