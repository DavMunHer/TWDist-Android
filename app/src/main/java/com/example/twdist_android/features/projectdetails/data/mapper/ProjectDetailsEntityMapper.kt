package com.example.twdist_android.features.projectdetails.data.mapper

import com.example.twdist_android.core.data.local.entity.ProjectEntity
import com.example.twdist_android.features.projectdetails.domain.model.Project
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName

fun Project.toEntity(): ProjectEntity = ProjectEntity(
    id = id,
    name = name.value,
    isFavorite = isFavorite,
    pendingTasks = 0
)

fun ProjectEntity.toProject(sectionIds: List<Long>): Result<Project> {
    val nameResult = ProjectName.create(name)
    if (nameResult.isFailure) return Result.failure(nameResult.exceptionOrNull()!!)
    return Project.create(
        id = id,
        name = nameResult.getOrThrow(),
        isFavorite = isFavorite,
        sectionIds = sectionIds
    )
}
