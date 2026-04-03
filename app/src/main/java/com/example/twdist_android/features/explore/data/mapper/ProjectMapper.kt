package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.domain.model.ProjectName

fun ProjectResponseDto.toDomain(): Project = Project.create(
    id = this.id,
    name = ProjectName.create(this.name).getOrThrow(), // We assume API data is valid
    isFavorite = false,
    pendingTasks = 0,
    sectionIds = emptyList()
).getOrThrow()

fun Project.toDto(): ProjectResponseDto = ProjectResponseDto(
    id = this.id,
    name = this.name.asString()
)