package com.example.twdist_android.features.explore.data.repository

import com.example.twdist_android.features.explore.data.remote.ExploreApi
import com.example.twdist_android.features.explore.domain.model.Section
import com.example.twdist_android.features.explore.domain.model.SectionName
import com.example.twdist_android.features.explore.domain.repository.SectionRepository
import javax.inject.Inject

class SectionRepositoryImpl @Inject constructor(
    private val api: ExploreApi
) : SectionRepository {

    override suspend fun getSectionsByProjectId(projectId: Long): Result<List<Section>> {
        return Result.failure(
            NotImplementedError("Section endpoints are not implemented in ExploreApi yet")
        )
    }

    override suspend fun createSection(projectId: Long, sectionName: SectionName): Result<Section> {
        return Result.failure(
            NotImplementedError("Section creation endpoint is not implemented in ExploreApi yet")
        )
    }

    override suspend fun addTaskIdToSection(sectionId: Long, taskId: String): Result<Section> {
        return Result.failure(
            NotImplementedError("Section task-id endpoint is not implemented in ExploreApi yet")
        )
    }

    override suspend fun replaceTaskIdInSection(
        sectionId: Long,
        oldTaskId: String,
        newTaskId: String
    ): Result<Section> {
        return Result.failure(
            NotImplementedError("Section task-id replace endpoint is not implemented in ExploreApi yet")
        )
    }

    override suspend fun removeTaskIdFromSection(sectionId: Long, taskId: String): Result<Section> {
        return Result.failure(
            NotImplementedError("Section task-id removal endpoint is not implemented in ExploreApi yet")
        )
    }
}
