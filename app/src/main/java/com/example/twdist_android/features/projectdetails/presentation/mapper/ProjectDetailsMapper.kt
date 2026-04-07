package com.example.twdist_android.features.projectdetails.presentation.mapper

import com.example.twdist_android.features.projectdetails.domain.model.ProjectAggregate
import com.example.twdist_android.features.projectdetails.presentation.model.ProjectDetailsUi
import com.example.twdist_android.features.projectdetails.presentation.model.SectionUi
import com.example.twdist_android.features.projectdetails.presentation.model.TaskUi

fun ProjectAggregate.toDetailsUi(): ProjectDetailsUi {
    return ProjectDetailsUi(
        id = project.id,
        name = project.name.asString(),
        isFavorite = project.isFavorite,
        sections = sections.map { section ->
            SectionUi(
                id = section.id,
                name = section.name.asString(),
                tasks = emptyList()
            )
        }
    )
}

fun ProjectDetailsUi.removingSection(sectionId: Long): ProjectDetailsUi =
    copy(sections = sections.filterNot { it.id == sectionId })

fun ProjectDetailsUi.renamingSection(sectionId: Long, name: String): ProjectDetailsUi =
    copy(sections = sections.map { if (it.id == sectionId) it.copy(name = name) else it })

fun ProjectDetailsUi.withSectionTasks(sectionId: Long, tasks: List<TaskUi>): ProjectDetailsUi =
    copy(sections = sections.map { if (it.id == sectionId) it.copy(tasks = tasks) else it })
