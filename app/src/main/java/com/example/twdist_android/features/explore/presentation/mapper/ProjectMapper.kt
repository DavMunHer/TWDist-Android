package com.example.twdist_android.features.explore.presentation.mapper

import com.example.twdist_android.features.explore.domain.model.Project
import com.example.twdist_android.features.explore.presentation.model.ProjectUi

fun Project.toUi(): ProjectUi {
    return ProjectUi(
        id = this.id,
        name = this.name.asString(),
        isFavorite = this.isFavorite,
        pendingTasks = this.pendingTasks
    )
}
