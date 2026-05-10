package com.example.twdist_android.features.explore.data.mapper

import com.example.twdist_android.core.data.local.entity.ProjectEntity
import com.example.twdist_android.features.explore.domain.model.ProjectSummary

/**
 * Persisted project summary row (counts are queried from tasks, never stored denormalised on project).
 */
fun ProjectSummary.toEntity(): ProjectEntity = ProjectEntity(
    id = id,
    name = name.value,
    isFavorite = isFavorite
)
