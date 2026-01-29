package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.features.explore.data.dto.ProjectDto
import com.example.twdist_android.features.explore.domain.model.Project

fun ProjectDto.toDomain(): Project = Project(
    id = this.id,
    name = this.name
)

fun Project.toDto(): ProjectDto = ProjectDto(
    id = this.id,
    name = this.name
)