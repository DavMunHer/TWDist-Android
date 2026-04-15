package com.example.twdist_android.features.explore.presentation.event

sealed class ExploreEvent {
    data object ToggleExpanded : ExploreEvent()
    data class CreateProject(val name: String) : ExploreEvent()
    data object LoadProjects : ExploreEvent()
    data class ToggleProjectFavorite(val projectId: Long) : ExploreEvent()
    data object ClearValidationErrors : ExploreEvent()
    data class ShowDeleteProjectConfirmation(val projectId: Long) : ExploreEvent()
    data object DismissDeleteProjectConfirmation : ExploreEvent()
    data object ConfirmDeleteProject : ExploreEvent()
}