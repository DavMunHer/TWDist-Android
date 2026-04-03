package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectName

fun ProjectResponseDto.toDomain(): Result<Project> {
    val nameResult = ProjectName.create(this.name)
    if (nameResult.isFailure) {
        return Result.failure(nameResult.exceptionOrNull()!!)
    }

    return Project.create(
        id = this.id,
        name = nameResult.getOrThrow(),
        isFavorite = false,
        pendingTasks = 0,
        sectionIds = emptyList()
    )
}

fun Project.toDto(): ProjectResponseDto = ProjectResponseDto(
    id = this.id,
    name = this.name.asString()
)