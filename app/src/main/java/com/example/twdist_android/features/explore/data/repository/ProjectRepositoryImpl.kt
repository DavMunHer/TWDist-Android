package com.example.twdist_android.features.explore.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.explore.data.dto.CreateProjectRequestDto
import com.example.twdist_android.features.explore.data.mapper.toDomainResponse
import com.example.twdist_android.features.explore.data.mapper.toDomainSummary
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: ExploreApi
) : ProjectRepository {

    override suspend fun getAllProjects(): Result<List<ProjectSummary>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val mappedProjects = api.getProjects().map { it.toDomainSummary() }
                val failure = mappedProjects.firstOrNull { it.isFailure }?.exceptionOrNull()
                if (failure != null) {
                    throw failure
                }
                mappedProjects.map { it.getOrThrow() }
            }
        }
    }

    override suspend fun createProject(projectName: ProjectName): Result<Project> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val request = CreateProjectRequestDto(name = projectName.asString())
                api.createProject(request).toDomainResponse().getOrThrow()
            }
        }
    }
}
