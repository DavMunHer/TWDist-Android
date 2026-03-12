package com.example.twdist_android.features.explore.presentation.mapper

import com.example.twdist_android.features.explore.domain.model.ProjectName

data class CreateProjectFormData(
    val name: String
)

fun CreateProjectFormData.toProjectName(): Result<ProjectName> {
    return ProjectName.create(name)
}
