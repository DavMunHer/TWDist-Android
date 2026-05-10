package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.core.data.local.entity.ProjectEntity
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName

fun ProjectSummary.toEntity(): ProjectEntity = ProjectEntity(
    id = id,
    name = name.value,
    isFavorite = isFavorite,
    pendingTasks = pendingTasks
)

fun ProjectEntity.toProjectSummary(): Result<ProjectSummary> {
    val nameResult = ProjectName.create(name)
    if (nameResult.isFailure) return Result.failure(nameResult.exceptionOrNull()!!)
    return Result.success(
        ProjectSummary(
            id = id,
            name = nameResult.getOrThrow(),
            isFavorite = isFavorite,
            pendingTasks = pendingTasks
        )
    )
}
