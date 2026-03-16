package com.example.twdist_android.features.explore.data.repository

import com.example.twdist_android.core.coroutines.runSuspendCatching
import com.example.twdist_android.features.explore.data.dto.CreateProjectRequestDto
import com.example.twdist_android.features.explore.data.mapper.toDomain
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val api: ExploreApi
) : ExploreRepository {
    override suspend fun getAllProjects(): Result<List<Project>> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                val response = api.getProjects()
                // Transform DTO to Domain
                response.map { it.toDomain() }
            }
        }
    }

    override suspend fun createProject(projectName: ProjectName): Result<Unit> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                // Create request DTO in data layer, UI/domain remain transport-agnostic
                val request = CreateProjectRequestDto(name = projectName.asString())
                api.createProject(request)
            }
        }
    }
}