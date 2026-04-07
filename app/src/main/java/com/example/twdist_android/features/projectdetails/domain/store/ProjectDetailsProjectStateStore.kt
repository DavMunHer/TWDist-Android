package com.example.twdist_android.features.projectdetails.domain.store

import com.example.twdist_android.features.projectdetails.domain.model.Project

interface ProjectDetailsProjectStateStore {
    fun upsert(project: Project)
    fun getById(projectId: Long): Project?
    fun remove(projectId: Long)
}
