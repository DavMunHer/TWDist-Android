package com.example.twdist_android.features.explore.domain.model

data class ProjectAggregate(
    val project: Project,
    val sections: List<Section>,
    // TODO: Add tasks when needed
)
