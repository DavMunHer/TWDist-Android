package com.example.twdist_android.features.explore.data.store.inmemory

import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.store.ProjectStateStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryProjectStore @Inject constructor() : ProjectStateStore {
    private val projectsById = linkedMapOf<Long, ProjectSummary>()

    override fun upsert(project: ProjectSummary) {
        synchronized(this) {
            projectsById[project.id] = project
        }
    }

    override fun upsertAll(projects: List<ProjectSummary>) {
        synchronized(this) {
            projects.forEach { project ->
                projectsById[project.id] = project
            }
        }
    }

    override fun getById(projectId: Long): ProjectSummary? {
        return synchronized(this) { projectsById[projectId] }
    }

    override fun getAll(): List<ProjectSummary> {
        return synchronized(this) { projectsById.values.toList() }
    }

    override fun remove(projectId: Long) {
        synchronized(this) {
            projectsById.remove(projectId)
        }
    }
}
