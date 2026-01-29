package com.example.twdist_android.features.explore.presentation.model

import com.example.twdist_android.features.explore.domain.model.Project

data class ExploreUiState(
    val isExpanded: Boolean = true,
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)