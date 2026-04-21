package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.twdist_android.features.favorite.presentation.model.FavoriteUndoAction

@Composable
internal fun FavoriteUndoSnackbarEffect(
    pendingUndo: FavoriteUndoAction?,
    snackbarHostState: SnackbarHostState,
    onUndo: (Long) -> Unit,
    onUndoHandled: (Long) -> Unit
) {
    LaunchedEffect(pendingUndo) {
        val undoAction = pendingUndo ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = "Removed from favorites",
            actionLabel = "Undo",
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            onUndo(undoAction.token)
        } else {
            onUndoHandled(undoAction.token)
        }
    }
}
