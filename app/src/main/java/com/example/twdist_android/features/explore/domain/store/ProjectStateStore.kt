package com.example.twdist_android.features.explore.domain.store

import com.example.twdist_android.features.explore.domain.model.ProjectSummary

interface ProjectStateStore {
    fun upsert(project: ProjectSummary)
    fun upsertAll(projects: List<ProjectSummary>)
    fun getById(projectId: Long): ProjectSummary?
    fun getAll(): List<ProjectSummary>
    fun remove(projectId: Long)
}
