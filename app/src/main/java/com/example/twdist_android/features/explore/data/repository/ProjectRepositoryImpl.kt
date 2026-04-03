package com.example.twdist_android.features.explore.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.explore.data.dto.CreateProjectRequestDto
import com.example.twdist_android.features.explore.data.mapper.toDomain
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: ExploreApi
) : ProjectRepository {

    override suspend fun getAllProjects(): Result<List<Project>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                api.getProjects().map { it.toDomain() }
            }
        }
    }

    override suspend fun createProject(projectName: ProjectName): Result<Project> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val request = CreateProjectRequestDto(name = projectName.asString())
                api.createProject(request).toDomain()
            }
        }
    }
}
