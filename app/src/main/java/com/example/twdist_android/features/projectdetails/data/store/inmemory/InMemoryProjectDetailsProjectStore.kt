package com.example.twdist_android.features.projectdetails.data.store.inmemory

import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.store.ProjectDetailsProjectStateStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryProjectDetailsProjectStore @Inject constructor() : ProjectDetailsProjectStateStore {
    private val projectsById = linkedMapOf<Long, Project>()

    override fun upsert(project: Project) {
        synchronized(this) { projectsById[project.id] = project }
    }

    override fun getById(projectId: Long): Project? {
        return synchronized(this) { projectsById[projectId] }
    }

    override fun remove(projectId: Long) {
        synchronized(this) { projectsById.remove(projectId) }
    }
}
