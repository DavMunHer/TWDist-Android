package com.example.twdist_android.features.projectdetails.presentation.mapper

import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUi
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi

fun ProjectAggregate.toDetailsUi(): ProjectDetailsUi {
    return ProjectDetailsUi(
        id = project.id,
        name = project.name.asString(),
        isFavorite = project.isFavorite,
        sections = sections.map { section ->
            SectionUi(
                name = section.name.asString(),
                tasks = section.taskIds
            )
        }
    )
}
