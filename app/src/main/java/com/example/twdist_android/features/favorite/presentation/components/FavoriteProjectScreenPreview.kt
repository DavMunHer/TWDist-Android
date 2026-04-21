package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectsUiState

@Preview(showBackground = true)
@Composable
internal fun FavoriteProjectScreenPreview() {
    FavoriteContent(
        uiState = FavoriteProjectsUiState(),
        onProjectClick = {},
        onUnfavoriteClick = {}
    )
}
