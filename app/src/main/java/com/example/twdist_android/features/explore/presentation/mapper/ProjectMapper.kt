package com.example.twdist_android.features.explore.presentation.mapper

import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import com.example.twdist_android.features.explore.presentation.model.ProjectUi

fun ProjectSummary.toUi(): ProjectUi {
    return ProjectUi(
        id = this.id,
        name = this.name.asString(),
        isFavorite = this.isFavorite,
        pendingTasks = this.pendingTasks
    )
}
