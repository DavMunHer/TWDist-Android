package com.example.twdist_android.features.explore.data.repository

import com.example.twdist_android.features.explore.data.dto.CreateProjectRequestDto
import com.example.twdist_android.features.explore.data.mapper.toDomain
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.repository.ExploreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val api: ExploreApi
) : ExploreRepository {
    override suspend fun getAllProjects(): Result<List<Project>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getProjects()
                // Transform DTO to Domain
                // Instead of use !! its better to use Result because its safer
                Result.success(response.map { it.toDomain() })
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun createProject(name: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Create here the request DTO, the viewmodel doesn't know that this exists
            val request = CreateProjectRequestDto(name = name)
            val response = api.createProject(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}