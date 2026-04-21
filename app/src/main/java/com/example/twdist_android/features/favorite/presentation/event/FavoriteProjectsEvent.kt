package com.example.twdist_android.features.favorite.presentation.event

sealed interface FavoriteProjectsEvent {
    data object LoadProjects : FavoriteProjectsEvent
    data class UnfavoriteProject(val projectId: Long) : FavoriteProjectsEvent
    data class UndoUnfavorite(val token: Long) : FavoriteProjectsEvent
    data class UndoMessageHandled(val token: Long) : FavoriteProjectsEvent
}
