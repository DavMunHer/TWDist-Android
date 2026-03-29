package com.example.twdist_android.features.explore.presentation.model

import com.example.twdist_android.features.explore.presentation.model.ProjectUi

data class ExploreUiState(
    val isExpanded: Boolean = true,
    val projects: List<ProjectUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val projectNameError: String? = null
)