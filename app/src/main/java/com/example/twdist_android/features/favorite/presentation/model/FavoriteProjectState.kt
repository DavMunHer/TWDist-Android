package com.example.twdist_android.features.favorite.presentation.model

import com.example.twdist_android.features.explore.presentation.model.ProjectUi

data class FavoriteProjectsUiState(
    val projects: List<ProjectUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val pendingUndo: FavoriteUndoAction? = null
)

data class FavoriteUndoAction(
    val token: Long
)