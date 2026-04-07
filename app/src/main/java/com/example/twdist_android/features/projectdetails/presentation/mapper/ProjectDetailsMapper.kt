package com.example.twdist_android.features.projectdetails.presentation.mapper

import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUi

fun ProjectAggregate.toDetailsUi(): ProjectDetailsUi {
    return ProjectDetailsUi(
        id = project.id,
        name = project.name.asString(),
        isFavorite = project.isFavorite,
        sections = sections.map { it.id.toString() }
    )
}

fun ProjectDetailsUi.removingSection(sectionId: Long): ProjectDetailsUi =
    this

fun ProjectDetailsUi.renamingSection(sectionId: Long, name: String): ProjectDetailsUi =
    this
