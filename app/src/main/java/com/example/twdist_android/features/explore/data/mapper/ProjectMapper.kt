package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.domain.model.Project

fun ProjectResponseDto.toDomain(): Project = Project(
    id = this.id,
    name = this.name
)

fun Project.toDto(): ProjectResponseDto = ProjectResponseDto(
    id = this.id,
    name = this.name
)